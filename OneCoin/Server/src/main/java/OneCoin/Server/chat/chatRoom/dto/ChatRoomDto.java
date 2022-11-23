package OneCoin.Server.chat.chatRoom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.mapstruct.Mapper;

import java.util.UUID;

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
