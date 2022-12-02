package OneCoin.Server.chat.entity;


import OneCoin.Server.chat.constant.MessageType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.id.GUIDGenerator;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {
    @JsonProperty("id")
    private String id;
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
