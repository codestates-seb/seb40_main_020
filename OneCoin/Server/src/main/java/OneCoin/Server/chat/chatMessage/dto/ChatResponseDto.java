package OneCoin.Server.chat.chatMessage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ChatResponseDto {
    private long chatRoomId;
    private String userDisplayName;
    private String message;
    private String chatAt;
}
