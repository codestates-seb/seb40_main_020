package OneCoin.Server.chat.intercepter;

import OneCoin.Server.chat.chatRoom.entity.UserInChatRoom;
import OneCoin.Server.chat.chatRoom.service.ChatRoomService;
import OneCoin.Server.chat.chatRoom.vo.UserInfoInChatRoom;
import OneCoin.Server.chat.constant.MessageType;
import OneCoin.Server.chat.chatMessage.publisher.RedisPublisher;
import OneCoin.Server.config.auth.utils.AuthorizationUtilsForWebSocket;
import OneCoin.Server.config.auth.utils.LoggedInUserInfoUtilsForWebSocket;
import OneCoin.Server.exception.BusinessLogicException;
import OneCoin.Server.exception.ExceptionCode;
import OneCoin.Server.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class MessageInterceptor implements ChannelInterceptor {
    private final LoggedInUserInfoUtilsForWebSocket loggedInUserInfoUtilsForWebSocket;
    private final ChatRoomService chatRoomService;
    private final RedisPublisher redisPublisher;
    private final AuthUtilForWebsocket authUtil;
    private final String AUTHORIZATION = "Authorization";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        StompCommand command = accessor.getCommand();
        String sessionId = accessor.getSessionId();
        if (StompCommand.CONNECT.equals(command)) {
            log.info("[CONNECT] start {}", sessionId);
            authenticate(accessor);
            log.info("[CONNECT] complete {}", sessionId);
        }
        if (StompCommand.SUBSCRIBE.equals(command)) {
            log.info("[SUBSCRIBE] start {}", sessionId);
            registerUserAndSendEnterMessage(accessor);
            log.info("[SUBSCRIBE] complete {}", sessionId);
        } else if (StompCommand.SEND.equals(command)) {
            log.info("[SEND] start {}", sessionId);
        } else if (StompCommand.UNSUBSCRIBE.equals(command)) {
            log.info("[UNSUBSCRIBE] start {}", sessionId);
            unregisterUserAndSendLeaveMessage(sessionId);
            log.info("[UNSUBSCRIBE] complete {}", sessionId);
        } else if (StompCommand.DISCONNECT.equals(command)) {
            log.info("[DISCONNECT] start {}", sessionId);
            unregisterUserAndSendLeaveMessage(sessionId);
            log.info("[DISCONNECT] complete {}", sessionId);
        }
        return message;
    }

    @EventListener
    public void onDisconnectEvent(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        log.info("[ABNORMAL DISCONNECT] unregister start : user {}", sessionId);
        unregisterUserAndSendLeaveMessage(sessionId);
        log.info("[ABNORMAL DISCONNECT] unregister complete : user {}", sessionId);
    }

    private void authenticate(StompHeaderAccessor accessor) {
        String accessToken = accessor.getFirstNativeHeader(AUTHORIZATION);
        if(accessToken == null) return;
        Authentication authentication = authUtil.authenticate(accessToken);
        accessor.setUser(authentication);
    }

    //구독시에는 유저 방 정보에 유저가 몇 명 있고, 누가 있는지만 저장
    private void registerUserAndSendEnterMessage(StompHeaderAccessor accessor) {
        Integer chatRoomId = parseRoomIdFromHeader(accessor); //채팅 구독이 아니면 null반환
        if (chatRoomId == null) return;
        User user = loggedInUserInfoUtilsForWebSocket.extractUser(accessor.getUser()); //비회원은 null을 반환
        String sessionId = accessor.getSessionId();
        if (user == null) { // 비회원이라면
            chatRoomService.saveUserToChatRoom(chatRoomId, sessionId);
            log.info("register user complete : unloggedInUser {} -> room {}", sessionId, chatRoomId);
            return;
        }
        chatRoomService.saveUserToChatRoom(chatRoomId, sessionId, user);
        redisPublisher.publishEnterOrLeaveMessage(MessageType.ENTER, chatRoomId, user);
        log.info("register user complete : user {} -> room {}", sessionId, chatRoomId);
    }

    private void unregisterUserAndSendLeaveMessage(String sessionId) {
        UserInfoInChatRoom user = chatRoomService.deleteUserFromChatRoom(sessionId);
        if(user.getUser() !=  null) {
            redisPublisher.publishEnterOrLeaveMessage(MessageType.LEAVE, user.getChatRoomId(), user.getUser());
        }
        log.info("unregister user complete : unloggedInUser {}", sessionId);
    }

    private Integer parseRoomIdFromHeader(StompHeaderAccessor accessor) {
        try {
            String destination = accessor.getDestination();
            String[] temp = destination.split("/");
            if (!temp[temp.length - 2].equals("rooms")) {
                return null;
            }
            return Integer.parseInt(temp[temp.length - 1]);
        } catch (Exception e) {
            throw new BusinessLogicException(ExceptionCode.INVALID_DESTINATION);
        }
    }


}
