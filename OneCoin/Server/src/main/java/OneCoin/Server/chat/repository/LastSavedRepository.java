package OneCoin.Server.chat.repository;

import OneCoin.Server.chat.entity.ChatMessage;
import OneCoin.Server.chat.utils.ChatRoomUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Repository
public class LastSavedRepository {
    private final RedisTemplate<String, ChatMessage> redisTemplate;
    private final ChatRoomUtils chatRoomUtils;
    private final ObjectMapper objectMapper;
    private ValueOperations<String, ChatMessage> operations;

    @PostConstruct
    private void init() {
        operations = redisTemplate.opsForValue();
    }

    public void save(Integer chatRoomId, ChatMessage chatMessage) {
        operations.set(chatRoomUtils.makeLastChatMessageKey(chatRoomId), chatMessage);
    }

    public ChatMessage get(Integer chatRoomId) {
        Object chatMessage = operations.get(chatRoomUtils.makeLastChatMessageKey(chatRoomId));
        if (chatMessage == null) return null;
        return objectMapper.convertValue(chatMessage, ChatMessage.class);
    }

    public void delete(Integer chatRoomId) {
        operations.getAndDelete(chatRoomUtils.makeLastChatMessageKey(chatRoomId));
    }
}
