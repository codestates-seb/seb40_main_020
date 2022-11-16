package OneCoin.Server.webSocket.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    public enum MessageType{ENTER, TALK}
    private MessageType type;
    private String sender;
    private String roomId;
    private String message;

    public void setSender(String sender) {
        this.sender = sender;
    }
}
