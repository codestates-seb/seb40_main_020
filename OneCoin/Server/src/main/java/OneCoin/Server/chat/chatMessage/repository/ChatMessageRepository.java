package OneCoin.Server.chat.chatMessage.repository;

import OneCoin.Server.chat.chatMessage.entity.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.lettuce.core.Limit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    //채팅방에 메시지 저장
    public void save(ChatMessage chatMessage) {
        String key = keyMaker(chatMessage.getChatRoomId());
        ZSetOperations<String, Object> operations = redisTemplate.opsForZSet();
        operations.add(key, chatMessage, System.currentTimeMillis());
//        operations.removeRange(chatRoomId, 5, -1);
    }

    public List<ChatMessage> getMessageFromRoomLimitN(Long chatRoomId, long limit) {
        String key = keyMaker(chatRoomId);
        ZSetOperations<String, Object> operations = redisTemplate.opsForZSet();
        Object results = operations.reverseRangeByScore(key, Long.MIN_VALUE, Long.MAX_VALUE, 0L, limit);
        return Arrays.asList(objectMapper.convertValue(results, ChatMessage[].class));
    }

    private String keyMaker(Long chatRoomId) {
        return "ChatRoom" + String.valueOf(chatRoomId) + "Message";
    }

}
