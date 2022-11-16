package OneCoin.Server.webSocket.util;

import OneCoin.Server.webSocket.dto.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MessageUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ChatMessage getObject(final String message) throws Exception {
        return objectMapper.readValue(message, ChatMessage.class);
    }

    public static String getString(final ChatMessage message) throws Exception {
        return objectMapper.writeValueAsString(message);
    }
}
