package OneCoin.Server.chat.chatMessage.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@Builder
@NoArgsConstructor
public class ChatResponseDto {
    private long chatRoomId;
    private String userDisplayName;
    private String message;
    private String chatAt;
}
