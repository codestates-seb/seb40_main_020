package OneCoin.Server.chat.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

//TODO:
// [ ] disconnect할 때 지워줘야함.
@RequiredArgsConstructor
@Repository
public class LastSentScoreRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private ValueOperations<String, String> operations;

    @PostConstruct
    private void init() {
        operations = redisTemplate.opsForValue();
    }

    public void save(String sessionId, String score) {
        operations.set(sessionId, score);
    }

    public String get(String sessionId) {
        return operations.get(sessionId);
    }

    public String delete(String sessionId) {
        return operations.getAndDelete(sessionId);
    }
    public double getNextScoreOfRecentlySent(String sessionId) {
        String recentScoreAsString = operations.get(sessionId);
        if (recentScoreAsString == null) {
            return Double.MAX_VALUE;
        }
        return Double.parseDouble(recentScoreAsString) - 1L;
    }
}
