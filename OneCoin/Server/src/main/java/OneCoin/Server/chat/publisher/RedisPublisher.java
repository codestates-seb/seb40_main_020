package OneCoin.Server.chat.publisher;

import OneCoin.Server.chat.dto.ChatResponseDto;
import OneCoin.Server.chat.entity.ChatMessage;
import OneCoin.Server.chat.mapper.ChatMapper;
import OneCoin.Server.chat.service.ChatService;
import OneCoin.Server.chat.constant.MessageType;
import OneCoin.Server.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisPublisher {
    private final RedisTemplate<Object, Object> redisTemplate;
    private final ChatService chatService;
    private final ChatMapper chatMapper;
    private final ChannelTopic channelTopic;

    public void publishEnterOrLeaveMessage(MessageType type, Integer chatRoomId, User user) {
        ChatMessage messageToUse = chatService.makeEnterOrLeaveChatMessage(type, chatRoomId, user);
        ChatResponseDto chatResponseDto = chatMapper.chatMessageToResponseDto(messageToUse);
        log.info("[PUBLISHER] {} message is ready for sending", type);
        log.info("[MESSAGE] {}", chatResponseDto);
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatResponseDto);
    }
}
