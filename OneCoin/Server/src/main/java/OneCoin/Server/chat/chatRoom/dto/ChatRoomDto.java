package OneCoin.Server.chat.chatRoom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class ChatRoomDto {
    private Long chatRoomId;
    private String name;
    private String nation;
    private Long numberOfChatters;
}
