package OneCoin.Server.chat.repository;

import OneCoin.Server.chat.entity.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepository {
    public final long NUMBER_OF_CHATS_TO_SHOW = 50L;
    private final RedisTemplate<String, ChatMessage> redisTemplate;
    private final ObjectMapper objectMapper;
    private final int MAX_CHAT_ROOM = 2;
    private final int TTL_IN_DAYS = 3;
    // <chatRoomKey, ChatMessage>
    private ListOperations<String, ChatMessage> operations;

    //채팅방에 메시지 저장
    @PostConstruct
    private void init() {
        operations = redisTemplate.opsForList();
        for (int chatRoomId = 1; chatRoomId <= MAX_CHAT_ROOM; chatRoomId++) {
            redisTemplate.expire(getKey(chatRoomId), TTL_IN_DAYS, TimeUnit.DAYS);
        }
    }

    public void save(ChatMessage chatMessage) {
        String key = getKey(chatMessage.getChatRoomId());
        String id = UUID.randomUUID().toString();
        chatMessage.setChatMessageId(id);
        operations.leftPush(key, chatMessage);
    }

    public void removeAllInChatRoom(Integer chatRoomId) {
        redisTemplate.delete(getKey(chatRoomId));
    }

    public List<ChatMessage> getMessageFromRoom(Integer chatRoomId) {
        String key = getKey(chatRoomId);
        Object results = operations.range(key, 0L, NUMBER_OF_CHATS_TO_SHOW - 1L);
        return Arrays.asList(objectMapper.convertValue(results, ChatMessage[].class));
    }

    public List<ChatMessage> findAll(Integer chatRoomId) {
        return operations.range(getKey(chatRoomId), 0L, -1L);
    }

    public List<ChatMessage> findAllAfter(Integer chatRoomId, Long index) {
        return operations.range(getKey(chatRoomId), 0, index);
    }

    public Long getIndex(Integer chatRoomId, ChatMessage chatMessage) {
        return operations.lastIndexOf(getKey(chatRoomId), chatMessage);
    }

    private String getKey(Integer chatRoomId) {
        return "ChatRoom" + String.valueOf(chatRoomId) + "Message";
    }

}
