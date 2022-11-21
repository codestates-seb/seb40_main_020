package OneCoin.Server.chat.redis;

import OneCoin.Server.chat.chatMessage.dto.ChatResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RedisPublisher {
    private final RedisTemplate<Object, Object> redisTemplate;

    public void publish(ChannelTopic topic, ChatResponseDto responseDto) {
        redisTemplate.convertAndSend(topic.getTopic(), responseDto);
    }
}
