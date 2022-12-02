package OneCoin.Server.chat.controller;

import OneCoin.Server.chat.constant.MessageType;
import OneCoin.Server.chat.dto.ChatResponseDto;
import OneCoin.Server.chat.entity.ChatMessage;
import OneCoin.Server.chat.mapper.ChatMapper;
import OneCoin.Server.chat.repository.vo.UserInfoInChatRoom;
import OneCoin.Server.chat.service.ChatRoomService;
import OneCoin.Server.chat.service.ChatService;
import OneCoin.Server.config.webSocketAuth.WebSocketAuthService;
import OneCoin.Server.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Slf4j
@RequiredArgsConstructor
public class RegisterController {
    private final ChatRoomService chatRoomService;
    private final ChatService chatService;
    private final ChatMapper chatMapper;
    private final RedisTemplate<Object, Object> redisTemplate;
    private final ChannelTopic channelTopic;
    private final WebSocketAuthService webSocketAuthService;

    //구독시에는 유저 방 정보에 유저가 몇 명 있고, 누가 있는지만 저장
    public void registerUserAndSendEnterMessage(String sessionId, Integer chatRoomId, User user) {
        if (chatRoomId == null) return; //채팅 서비스가 아닌 경우
        chatRoomService.saveUserInChatRoom(chatRoomId, sessionId, user);
        if (user != null && chatRoomService.isUserInChatRoom(chatRoomId, user.getEmail()) ) { //로그인한 유저인 경우
            ChatMessage messageToUse = chatService.makeEnterOrLeaveChatMessage(MessageType.ENTER, chatRoomId, user);
            ChatResponseDto chatResponseDto = chatMapper.chatMessageToResponseDto(messageToUse);
            redisTemplate.convertAndSend(channelTopic.getTopic(), chatResponseDto);
        }
    }

    public void unregisterUserAndSendLeaveMessage(String sessionId) {
        UserInfoInChatRoom user = chatRoomService.deleteUserFromChatRoom(sessionId);
        if (user.getUser() != null) { //로그인한 유저인 경우
            ChatMessage messageToUse = chatService.makeEnterOrLeaveChatMessage(MessageType.LEAVE, user.getChatRoomId(), user.getUser());
            ChatResponseDto chatResponseDto = chatMapper.chatMessageToResponseDto(messageToUse);
            redisTemplate.convertAndSend(channelTopic.getTopic(), chatResponseDto);
        }
    }

    public void authenticate(StompHeaderAccessor accessor) {
        webSocketAuthService.authenticate(accessor);
    }

    @EventListener
    public void onDisconnectEvent(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        unregisterUserAndSendLeaveMessage(sessionId);
    }
}
