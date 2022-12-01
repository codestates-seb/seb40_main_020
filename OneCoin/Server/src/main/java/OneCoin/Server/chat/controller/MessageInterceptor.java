package OneCoin.Server.chat.controller;

import OneCoin.Server.chat.constant.MessageType;
import OneCoin.Server.chat.dto.ChatResponseDto;
import OneCoin.Server.chat.entity.ChatMessage;
import OneCoin.Server.chat.mapper.ChatMapper;
import OneCoin.Server.chat.service.ChatRoomService;
import OneCoin.Server.chat.service.ChatService;
import OneCoin.Server.config.webSocketAuth.WebSocketAuthService;
import OneCoin.Server.chat.repository.vo.UserInfoInChatRoom;
import OneCoin.Server.config.auth.utils.UserUtilsForWebSocket;
import OneCoin.Server.exception.BusinessLogicException;
import OneCoin.Server.exception.ExceptionCode;
import OneCoin.Server.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@RequiredArgsConstructor
public class MessageInterceptor implements ChannelInterceptor {
    private final UserUtilsForWebSocket userUtilsForWebSocket;
    private final ChatRoomService chatRoomService;
    private final WebSocketAuthService webSocketAuthService;
    private final ChatService chatService;
    private final ChatMapper chatMapper;
    private final RedisTemplate<Object, Object> redisTemplate;
    private final ChannelTopic channelTopic;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        StompCommand command = accessor.getCommand();
        String sessionId = accessor.getSessionId();
        if (StompCommand.CONNECT.equals(command)) {
            log.info("[CONNECT] start {}", sessionId);
            webSocketAuthService.authenticate(accessor);
            log.info("[CONNECT] complete {}", sessionId);
        } else if (StompCommand.SUBSCRIBE.equals(command)) {
            log.info("[SUBSCRIBE] start {}", sessionId);
            Integer chatRoomId = parseRoomIdFromHeader(accessor); //채팅 구독이 아니면 null반환
            User user = userUtilsForWebSocket.extractUser(accessor.getUser()); //비회원은 null을 반환
            registerUserAndSendEnterMessage(sessionId, chatRoomId, user);
            log.info("[SUBSCRIBE] complete {}", sessionId);
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


    //구독시에는 유저 방 정보에 유저가 몇 명 있고, 누가 있는지만 저장
    private void registerUserAndSendEnterMessage(String sessionId, Integer chatRoomId, User user) {
        if (chatRoomId == null) return; //채팅 서비스가 아닌 경우
        chatRoomService.saveUserToChatRoom(chatRoomId, sessionId, user);
        if (user != null) { //로그인한 유저인 경우
            ChatMessage messageToUse = chatService.makeEnterOrLeaveChatMessage(MessageType.ENTER, chatRoomId, user);
            ChatResponseDto chatResponseDto = chatMapper.chatMessageToResponseDto(messageToUse);
            redisTemplate.convertAndSend(channelTopic.getTopic(), chatResponseDto);
        }
    }

    private void unregisterUserAndSendLeaveMessage(String sessionId) {
        UserInfoInChatRoom user = chatRoomService.deleteUserFromChatRoom(sessionId);
        if (user.getUser() != null) { //로그인한 유저인 경우
            ChatMessage messageToUse = chatService.makeEnterOrLeaveChatMessage(MessageType.LEAVE, user.getChatRoomId(), user.getUser());
            ChatResponseDto chatResponseDto = chatMapper.chatMessageToResponseDto(messageToUse);
            redisTemplate.convertAndSend(channelTopic.getTopic(), chatResponseDto);
        }
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
