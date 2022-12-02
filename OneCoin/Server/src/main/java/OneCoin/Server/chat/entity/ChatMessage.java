package OneCoin.Server.chat.entity;


import OneCoin.Server.chat.constant.MessageType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ChatMessage {
    @Id
    @JsonProperty("id")
    private String chatMessageId;
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
