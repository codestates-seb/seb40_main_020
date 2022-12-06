package OneCoin.Server.chat.controller;

import OneCoin.Server.chat.dto.ChatRequestDto;
import OneCoin.Server.chat.dto.ChatResponseDto;
import OneCoin.Server.chat.entity.ChatMessage;
import OneCoin.Server.chat.mapper.ChatMapper;
import OneCoin.Server.chat.service.ChatService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
@Slf4j
public class ChatController {
    private final ChatService chatService;
    private final ChatMapper chatMapper;
    private final SimpMessagingTemplate messagingTemplate;
    private final RedisTemplate<Object, Object> redisTemplate;
    private final ChannelTopic channelTopic;

    @MessageMapping("/rooms")
    public void sendMessage(ChatRequestDto requestMessage, StompHeaderAccessor headerAccessor) {
        log.info("[SEND] start {}", headerAccessor.getSessionId());
        ChatMessage convertedChatMessage = chatMapper.requestDtoToChatMessage(requestMessage);
        ChatMessage chatMessage = chatService.setInfoAndSaveMessage(convertedChatMessage, headerAccessor.getUser());
        ChatResponseDto chatResponseDto = chatMapper.chatMessageToResponseDto(chatMessage);
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatResponseDto);
        log.info("[SEND] complete {}", headerAccessor.getSessionId());
    }
}

