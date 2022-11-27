package OneCoin.Server.chat.chatRoom.repository;

import OneCoin.Server.chat.chatRoom.entity.ChatRoom;
import OneCoin.Server.exception.BusinessLogicException;
import OneCoin.Server.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private SetOperations<String, Object> operations;
    private final String KEY_FOR_CHAT_ROOMS = "chatRooms";
    private final String PREFIX_OF_KEY = "ChatRoom";
    private final String SUFFIX_OF_KEY = "Session";


    @PostConstruct
    private void init() {
        operations = redisTemplate.opsForSet();
    }

    public void saveSessionIdToChatRoom(Integer chatRoomId, String sessionId) {
        boolean isValid = doesChatRoomExist(chatRoomId);
        if (!isValid) {
            makeChatRoom(chatRoomId);
        }
        operations.add(getChatRoomKey(chatRoomId), sessionId);
    }

    public long getNumberOfSessionConnectedToChatRoom(Integer chatRoomId) {
        verifyChatRoom(chatRoomId);
        return operations.size(getChatRoomKey(chatRoomId));
    }
    // return 0 when nothing is removed;
    public long deleteSessionFromChatRoom(Integer chatRoomId, String sessionId) {
        verifyChatRoom(chatRoomId);
        return operations.remove(getChatRoomKey(chatRoomId), sessionId);
    }

    public List<ChatRoom> findChatRooms() {
       Set<Object> chatRooms = operations.members(KEY_FOR_CHAT_ROOMS);
       return chatRooms.stream().map(c -> {
           Integer chatRoomId = parseChatRoomId((String) c);
           long numberOfSessions = getNumberOfSessionConnectedToChatRoom(chatRoomId);
           return ChatRoom.builder()
                   .chatRoomId(chatRoomId)
                   .numberOfChatters(numberOfSessions)
                   .build();
       }).collect(Collectors.toList());
    }

    public void deleteChatRoom(Integer chatRoomId) {
        verifyChatRoom(chatRoomId);
        redisTemplate.delete(getChatRoomKey(chatRoomId));
        operations.remove(KEY_FOR_CHAT_ROOMS, getChatRoomKey(chatRoomId));
    }

    public void makeChatRoom(Integer chatRoomId) {
        Set<Object> chatRooms = operations.members(KEY_FOR_CHAT_ROOMS);
        int numberOfChatRooms = chatRooms.size();
        if(numberOfChatRooms + 1 != chatRoomId) {
            throw new BusinessLogicException(ExceptionCode.INVALID_CHAT_ROOM_ID);
        }
        operations.add(KEY_FOR_CHAT_ROOMS, getChatRoomKey(chatRoomId));
    }

    public boolean doesChatRoomExist(Integer chatRoomId) {
        return operations.isMember(KEY_FOR_CHAT_ROOMS, getChatRoomKey(chatRoomId));
    }

    private String getChatRoomKey(Integer chatRoomId) {
        return PREFIX_OF_KEY + String.valueOf(chatRoomId) + SUFFIX_OF_KEY;
    }
    private Integer parseChatRoomId(String key) {
        String chatRoomIdAsString = key.replace(PREFIX_OF_KEY, "");
        chatRoomIdAsString = chatRoomIdAsString.replace(SUFFIX_OF_KEY, "");
        return Integer.parseInt(chatRoomIdAsString);
    }
    public void verifyChatRoom(Integer chatRoomId) {
        boolean isValid = doesChatRoomExist(chatRoomId);
        if(!isValid) {
            throw new BusinessLogicException(ExceptionCode.INVALID_CHAT_ROOM_ID);
        }
    }
}
