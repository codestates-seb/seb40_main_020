package OneCoin.Server.chat.repository;

import OneCoin.Server.chat.entity.ChatMessage;
import OneCoin.Server.exception.BusinessLogicException;
import OneCoin.Server.exception.ExceptionCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ChatMessageRepository {
    @Getter
    private final Long NUMBER_OF_CHATS_TO_SHOW = 30L;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final int MAX_CHAT_ROOM = 2;
    private final int TTL_IN_DAYS = 2;
    // <chatRoomKey, ChatMessage>
    private ZSetOperations<String, Object> operations;

    //채팅방에 메시지 저장
    @PostConstruct
    private void init() {
        operations = redisTemplate.opsForZSet();
        for (int chatRoomId = 1; chatRoomId <= MAX_CHAT_ROOM; chatRoomId++) {
            redisTemplate.expire(getKey(chatRoomId), TTL_IN_DAYS, TimeUnit.MINUTES);
        }
    }

    public void save(ChatMessage chatMessage) {
        String key = getKey(chatMessage.getChatRoomId());
        operations.add(key, chatMessage, System.currentTimeMillis());
    }

    public void removeAllInChatRoom(Integer chatRoomId) {
        redisTemplate.delete(getKey(chatRoomId));
    }

    public List<ChatMessage> getMessagesFromRoomByScore(Integer chatRoomId, double scoreOfLastChat, double recentScore) {
        String key = getKey(chatRoomId);
        Set<Object> result = operations.reverseRangeByScore(key, scoreOfLastChat, recentScore);
        return objectToList(result);
    }

    public Double getScoreOfLastChatWithLimitN(Integer chatRoomId, double recentScore) {
        String key = getKey(chatRoomId);
        Set<ZSetOperations.TypedTuple<Object>> scores = operations.reverseRangeByScoreWithScores(
                key, Double.MIN_VALUE, recentScore, 0L, NUMBER_OF_CHATS_TO_SHOW);
        if (scores.size() == 0) return null;
        return scores.stream().skip(scores.size() - 1).findFirst().get().getScore();
    }

    public List<ChatMessage> findAll(Integer chatRoomId) {
        Object results = operations.reverseRange(getKey(chatRoomId), 0, -1);
        return objectToList(results);
    }

    public List<ChatMessage> findAllInAscOrder(Integer chatRoomId) {
        Object results = operations.range(getKey(chatRoomId), 0, -1);
        return objectToList(results);
    }

    private String getKey(Integer chatRoomId) {
        return "ChatRoom" + String.valueOf(chatRoomId) + "Message";
    }

    private List<ChatMessage> objectToList(Object obj) {
        if (obj == null) return null;
        return Arrays.asList(objectMapper.convertValue(obj, ChatMessage[].class));
    }

    public List<ChatMessage> findByScoreInAcsOrder(Integer chatRoomId, double from, double to) {
        String key = getKey(chatRoomId);
        Object result =  operations.rangeByScore(key, from + 1, to);
        return  objectToList(result);
    }

    public Double getScoreOfLatestChat(Integer chatRoomId, double from) {
        String key = getKey(chatRoomId);
        Set<ZSetOperations.TypedTuple<Object>> scores = operations.reverseRangeByScoreWithScores(
                key, from, Double.MAX_VALUE);
        if (scores == null || scores.size() == 0) {
            log.info(ExceptionCode.NO_CHAT_IN_CACHE_EXIST.getDescription());
            return null;
        }
        return scores.stream().findFirst().get().getScore();
    }

    public List<ChatMessage> getWithScore(Integer chatRoomid, double score) {
        Object result = operations.rangeByScore(getKey(chatRoomid), score, score);
        return objectToList(result);
    }

}
