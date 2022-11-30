package OneCoin.Server.chat.chatRoom.utils;

import org.springframework.stereotype.Component;

@Component
public class ChatRoomUtils {
    public final String KEY_FOR_CHAT_ROOMS = "chatRooms";
    private final String PREFIX_OF_KEY = "ChatRoom";
    private final String SUFFIX_OF_KEY = "Session";

    public Integer parseChatRoomId(String key) {
        String chatRoomIdAsString = key.replace(PREFIX_OF_KEY, "");
        chatRoomIdAsString = chatRoomIdAsString.replace(SUFFIX_OF_KEY, "");
        return Integer.parseInt(chatRoomIdAsString);
    }

    public String getKey(Integer chatRoomId) {
        return PREFIX_OF_KEY + String.valueOf(chatRoomId) + SUFFIX_OF_KEY;
    }
}
