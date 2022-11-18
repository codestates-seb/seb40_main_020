package OneCoin.Server.chat.chatMessage.dto;

import OneCoin.Server.chat.constant.MessageType;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {
    @NotBlank
    private MessageType type;
    @NotBlank
    private String sender;
    @NotBlank
    private Long senderId;
    @NotBlank
    private Long roomId;
    @NotBlank
    private String message;

    public void setSender(String sender) {
        this.sender = sender;
    }
}
