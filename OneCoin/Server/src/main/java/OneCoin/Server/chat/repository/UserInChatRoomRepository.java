package OneCoin.Server.chat.repository;

import OneCoin.Server.chat.entity.UserInChatRoom;
import OneCoin.Server.chat.utils.ChatRoomUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserInChatRoomRepository {
    private final ObjectMapper objectMapper;
    private final ChatRoomUtils chatRoomUtils;
    private final RedisTemplate<String, Object> redisTemplate;
    //<ChatRoomIdKey, SessionId, UserInChatRoom>
    private HashOperations<String, String, Object> hashOperations;

    @PostConstruct
    private void init() {
        hashOperations = redisTemplate.opsForHash();
    }

    public void addUser(Integer chatRoomId, String sessionId, UserInChatRoom user) {
        hashOperations.put(chatRoomUtils.getKey(chatRoomId), sessionId, user);
    }

    // return 0 when nothing is removed;
    public long removeUserBySessionId(String chatRoomKey, String sessionId) {
        return hashOperations.delete(chatRoomKey, sessionId);
    }

    public long getNumberOfUserInChatRoom(Integer chatRoomId) {
        return hashOperations.size(chatRoomUtils.getKey(chatRoomId));
    }

    public void removeAllInChatRoom(Integer chatRoomId) {
        redisTemplate.delete(chatRoomUtils.getKey(chatRoomId));
    }

    public List<UserInChatRoom> findAllByChatRoomId(Integer chatRoomId) {
        return Arrays.asList(objectMapper.convertValue(hashOperations.values(chatRoomUtils.getKey(chatRoomId)), UserInChatRoom[].class));
    }

    public boolean contain(String chatRoomIdKey, String sessionId) {
        return hashOperations.hasKey(chatRoomIdKey, sessionId);
    }

    public UserInChatRoom findBySessionId(String chatRoomIdKey, String sessionId) {
        return objectMapper.convertValue(hashOperations.get(chatRoomIdKey, sessionId), UserInChatRoom.class);
    }
}
