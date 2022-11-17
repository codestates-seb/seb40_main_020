package OneCoin.Server.chat.chatRoom.mapper;

import OneCoin.Server.chat.chatRoom.dto.ChatRoomDto;
import OneCoin.Server.chat.chatRoom.entity.ChatRoom;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatRoomMapper {
    ChatRoomDto chatRoomToChatRoomDto(ChatRoom chatRoom);
    List<ChatRoomDto> chatRoomListToChatRoomDtoList(List<ChatRoom> chatRooms);
}
