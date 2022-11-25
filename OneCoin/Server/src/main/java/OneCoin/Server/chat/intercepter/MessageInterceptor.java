package OneCoin.Server.chat.intercepter;

import OneCoin.Server.chat.chatRoom.entity.UserInChatRoomInMemory;
import OneCoin.Server.chat.chatRoom.service.ChatRoomInMemoryService;
import OneCoin.Server.chat.constant.MessageType;
import OneCoin.Server.chat.publisher.RedisPublisher;
import OneCoin.Server.config.auth.utils.AuthorizationUtilsForWebSocket;
import OneCoin.Server.config.auth.utils.LoggedInUserInfoUtilsForWebSocket;
import OneCoin.Server.exception.BusinessLogicException;
import OneCoin.Server.exception.ExceptionCode;
import OneCoin.Server.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class MessageInterceptor implements ChannelInterceptor {
    private final LoggedInUserInfoUtilsForWebSocket loggedInUserInfoUtilsForWebSocket;
    private final AuthorizationUtilsForWebSocket authorizationUtils;
    private final ChatRoomInMemoryService chatRoomInMemoryService;
    private final RedisPublisher redisPublisher;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        StompCommand command = accessor.getCommand();
        if (StompCommand.CONNECT.equals(command)) {
            log.info("[CONNECT] start {}", accessor.getSessionId());
            authorizationUtils.verifyAuthorization(accessor);
            log.info("[CONNECT] complete {}", accessor.getSessionId());
        } else if (StompCommand.SUBSCRIBE.equals(command)) {
            log.info("[SUBSCRIBE] start {}", accessor.getSessionId());
            registerUserAndSendEnterMessage(accessor);
            log.info("[SUBSCRIBE] complete {}", accessor.getSessionId());
        } else if (StompCommand.SEND.equals(command)) {
            log.info("[SEND] start {}", accessor.getSessionId());
        } else if (StompCommand.UNSUBSCRIBE.equals(command)) {
            log.info("[UNSUBSCRIBE] start {}", accessor.getSessionId());
            //TODO
            log.info("[UNSUBSCRIBE] complete {}", accessor.getSessionId());
        } else if (StompCommand.DISCONNECT.equals(command)) {
            log.info("[DISCONNECT] start {}", accessor.getSessionId());
            unregisterUserAndSendLeaveMessage(accessor);
            log.info("[DISCONNECT] complete {}", accessor.getSessionId());
        }
        return message;
    }

    //구독시에는 유저 방 정보에 유저가 몇 명 있고, 누가 있는지만 저장
    private void registerUserAndSendEnterMessage(StompHeaderAccessor accessor) {
        User user = loggedInUserInfoUtilsForWebSocket.extractUser(accessor.getUser()); //비회원은 null을 반환
        Long chatRoomId = parseRoomIdFromHeader(accessor); //채팅 구독이 아니면 null반환
        if (chatRoomId == null) return;
        chatRoomInMemoryService.increaseNumberOfChatters(chatRoomId);
        if (user != null) {
            chatRoomInMemoryService.saveUserInChatRoom(chatRoomId, user.getUserId(), user.getDisplayName(), user.getEmail());
            redisPublisher.publishEnterOrLeaveMessage(MessageType.ENTER, chatRoomId, user);
            log.info("register user complete : user {} -> room {}", user.getUserId(), chatRoomId);
            return;
        }
        log.info("register user complete : unloggedInUser -> room {}",chatRoomId);
    }

    private void unregisterUserAndSendLeaveMessage(StompHeaderAccessor accessor) {
        User user = loggedInUserInfoUtilsForWebSocket.extractUser(accessor.getUser()); //비회원은 널
        if (user == null) return;
        List<UserInChatRoomInMemory> usersInChatRoom = chatRoomInMemoryService.findByUserId(user.getUserId());
        if (usersInChatRoom.size() == 0) return;
        chatRoomInMemoryService.deleteUserInChatRoom(usersInChatRoom);
        usersInChatRoom.stream().forEach(userInChatRoomInMemory -> {
            Long chatRoomId = userInChatRoomInMemory.getChatRoomId();
            chatRoomInMemoryService.decreaseNumberOfChatters(chatRoomId);
            redisPublisher.publishEnterOrLeaveMessage(MessageType.LEAVE, chatRoomId, user);
            log.info("unregister user complete : user{} <- room {}", user.getUserId(), chatRoomId);
        });
    }

    private Long parseRoomIdFromHeader(StompHeaderAccessor accessor) {
        try {
            String destination = accessor.getDestination();
            String[] temp = destination.split("/");
            if (!temp[temp.length - 2].equals("rooms")) {
                return null;
            }
            return Long.parseLong(temp[temp.length - 1]);
        } catch (Exception e) {
            throw new BusinessLogicException(ExceptionCode.INVALID_DESTINATION);
        }
    }
}
