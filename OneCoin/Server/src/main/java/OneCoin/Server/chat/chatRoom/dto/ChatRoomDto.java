package OneCoin.Server.chat.chatRoom.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.mapstruct.Mapper;

import java.util.UUID;

@AllArgsConstructor
public class ChatRoomDto {
    private Long chatRoomId;
    private String name;
    private String nation;
    private Long numberOfChatters;
}
