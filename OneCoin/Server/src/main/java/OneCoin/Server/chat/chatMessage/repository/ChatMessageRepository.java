package OneCoin.Server.chat.chatMessage.repository;

import OneCoin.Server.chat.chatMessage.entity.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepository {
    private final RedisTemplate<String, ChatMessage> redisTemplate;
    // <chatRoomKey, ChatMessage>
    private ZSetOperations<String, ChatMessage> operations;
    private final ObjectMapper objectMapper;

    //채팅방에 메시지 저장
    @PostConstruct
    private void init() {
        operations = redisTemplate.opsForZSet();
    }
    public void save(ChatMessage chatMessage) {
        String key = makeKey(chatMessage.getChatRoomId());
        operations.add(key, chatMessage, System.currentTimeMillis());
//        operations.removeRange(chatRoomId, 5, -1);
    }

    public List<ChatMessage> getMessageFromRoomLimitN(Integer chatRoomId, Long limit) {
        String key = makeKey(chatRoomId);
        Object results = operations.reverseRangeByScore(key, Long.MIN_VALUE, Long.MAX_VALUE, 0L, limit);
        return Arrays.asList(objectMapper.convertValue(results, ChatMessage[].class));
    }

    public void removeAllInChatRoom(Integer chatRoomId) {
        operations.removeRange(makeKey(chatRoomId), Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private String makeKey(Integer chatRoomId) {
        return "ChatRoom" + String.valueOf(chatRoomId) + "Message";
    }

}
