package OneCoin.Server.chat.chatMessage.controller;

import OneCoin.Server.chat.chatMessage.dto.ChatRequestDto;
import OneCoin.Server.chat.chatMessage.dto.ChatResponseDto;
import OneCoin.Server.chat.chatMessage.entity.ChatMessage;
import OneCoin.Server.chat.chatMessage.mapper.ChatMapper;
import OneCoin.Server.chat.chatMessage.service.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final ChatMapper chatMapper;
    private final RedisTemplate<Object, Object> redisTemplate;
    private final ChannelTopic channelTopic;

    @MessageMapping("/rooms")//여기에는 @Payload가 생략되어 있음. body를 객체로 mapping시켜줌
    public void message(ChatRequestDto requestMessage) {
        ChatMessage convertedChatMessage = chatMapper.requestDtoToChatMessage(requestMessage);
        ChatMessage chatMessage = chatService.deligate(requestMessage.getType(), convertedChatMessage);
        ChatResponseDto chatResponseDto = chatMapper.chatMessageToResponseDto(chatMessage);
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatResponseDto);
    }
}

