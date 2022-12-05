package OneCoin.Server.chat.repository;

import OneCoin.Server.chat.entity.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
public class ChatMessageRepositoryImple {
    @Getter
    private final Long NUMBER_OF_CHATS_TO_SHOW = 10L;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final int MAX_CHAT_ROOM = 2;
    private final int TTL_IN_DAYS = 3;
    // <chatRoomKey, ChatMessage>
    private ZSetOperations<String, Object> operations;
    private final LastSentScoreRepository lastSentScoreRepository;

    //채팅방에 메시지 저장
    @PostConstruct
    private void init() {
        operations = redisTemplate.opsForZSet();
        for (int chatRoomId = 1; chatRoomId <= MAX_CHAT_ROOM; chatRoomId++) {
            redisTemplate.expire(getKey(chatRoomId), TTL_IN_DAYS, TimeUnit.DAYS);
        }
    }

    public void save(ChatMessage chatMessage) {
        String key = getKey(chatMessage.getChatRoomId());
        operations.add(key, chatMessage, System.currentTimeMillis());
    }

    public void removeAllInChatRoom(Integer chatRoomId) {
        redisTemplate.delete(getKey(chatRoomId));
    }

    public List<ChatMessage> getMessagesFromRoom(Integer chatRoomId, String sessionId) {
        String key = getKey(chatRoomId);
        double recentScore = getNextScoreOfRecentlySent(sessionId);
        Double scoreOfLastChat = getScoreOfLastChat(key, recentScore);
        Set<Object> result = operations.reverseRangeByScore(key, scoreOfLastChat, recentScore);
        lastSentScoreRepository.save(sessionId, scoreOfLastChat.toString());
        return objectToList(result);
    }

    private double getNextScoreOfRecentlySent(String sessionId) {
        String recentScoreAsString = lastSentScoreRepository.get(sessionId);
        if (recentScoreAsString == null) {
            return Double.MAX_VALUE;
        }
        return Double.parseDouble(recentScoreAsString) - 1L;
    }

    private Double getScoreOfLastChat(String key, double recentScore) {
        Set<ZSetOperations.TypedTuple<Object>> scores = operations.reverseRangeByScoreWithScores(
                key, Long.MIN_VALUE, recentScore, 0L, NUMBER_OF_CHATS_TO_SHOW);
        return scores.stream().skip(scores.size() - 1).findFirst().get().getScore();
    }
//
//    public List<ChatMessage> findAll(Integer chatRoomId) {
//        Object results = operations.range(getKey(chatRoomId), 0L, -1L);
//        return objectToList(results);
//    }
//    //인덱스 미포함
//    public List<ChatMessage> findAllAfter(Integer chatRoomId, Long index) {
//        Object results = operations.range(getKey(chatRoomId), index + 1L, Long.MAX_VALUE);
//        return objectToList(results);
//    }
//
//    public Long getIndex(Integer chatRoomId, ChatMessage chatMessage) {
//        return operations.lastIndexOf(getKey(chatRoomId), chatMessage);
//    }
//
    private String getKey(Integer chatRoomId) {
        return "ChatRoom" + String.valueOf(chatRoomId) + "Message";
    }

    private List<ChatMessage> objectToList(Object obj) {
        if(obj == null) return null;
        return Arrays.asList(objectMapper.convertValue(obj, ChatMessage[].class));
    }

}
