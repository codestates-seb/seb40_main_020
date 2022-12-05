package OneCoin.Server.chat.repository;

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
    private final RedisTemplate<String, String> redisTemplate;
    private final ChatRoomUtils chatRoomUtils;
    private final ObjectMapper objectMapper;
    private ValueOperations<String, String> operations;

    @PostConstruct
    private void init() {
        operations = redisTemplate.opsForValue();
    }

    public void save(Integer chatRoomId, String score) {
        operations.set(chatRoomUtils.makeLastChatMessageKey(chatRoomId), score);
    }

    public String get(Integer chatRoomId) {
        String result = operations.get(chatRoomUtils.makeLastChatMessageKey(chatRoomId));
        if (result == null) return null;
        return result;
    }

    public void delete(Integer chatRoomId) {
        operations.getAndDelete(chatRoomUtils.makeLastChatMessageKey(chatRoomId));
    }
}
