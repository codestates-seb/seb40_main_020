package OneCoin.Server.chat.chatMessage.dto;

import OneCoin.Server.chat.constant.MessageType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
@NoArgsConstructor
public class ChatResponseDto {
    private MessageType type;
    private long chatRoomId;
    private String userDisplayName;
    private String message;
    private String chatAt;
    private Long userId;
}
