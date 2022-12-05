package OneCoin.Server.chat.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Repository
public class LastSentScoreRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private ValueOperations<String, String> operations;
    private final long TTL = 1;

    @PostConstruct
    private void init() {
        operations = redisTemplate.opsForValue();
    }

    public void save(String sessionId, String score) {
        operations.set(sessionId, score);
        redisTemplate.expire(score, TTL, TimeUnit.DAYS);
    }

    public String get(String sessionId) {
        return operations.get(sessionId);
    }

    public String delete(String sessionId) {
        return operations.getAndDelete(sessionId);
    }
}
