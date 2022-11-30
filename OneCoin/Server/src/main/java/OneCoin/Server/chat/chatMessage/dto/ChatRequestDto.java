package OneCoin.Server.chat.chatMessage.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatRequestDto {
    @NotBlank
    private Integer chatRoomId;
    @NotBlank
    private String message;
}
