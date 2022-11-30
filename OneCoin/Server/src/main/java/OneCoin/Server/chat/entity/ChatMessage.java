package OneCoin.Server.chat.entity;


import OneCoin.Server.chat.constant.MessageType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {
    @JsonProperty("type")
    private MessageType type;
    @JsonProperty("message")
    private String message;
    @JsonProperty("chat_at")
    private String chatAt;
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("chat_room_id")
    private Integer chatRoomId;
    @JsonProperty("user_display_name")
    private String userDisplayName;
}
