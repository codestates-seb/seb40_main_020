package OneCoin.Server.chat.chatMessage.dto;

import OneCoin.Server.chat.constant.MessageType;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {
    private MessageType type;
    private String sender;
    private String roomId;
    private String message;

    public void setSender(String sender) {
        this.sender = sender;
    }
}
