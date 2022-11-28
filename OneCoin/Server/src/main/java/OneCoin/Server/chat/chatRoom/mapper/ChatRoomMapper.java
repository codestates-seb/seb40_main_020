package OneCoin.Server.chat.chatRoom.mapper;

import OneCoin.Server.chat.chatRoom.dto.UsersInRoomResponseDto;
import OneCoin.Server.chat.chatRoom.entity.UserInChatRoom;
import OneCoin.Server.user.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatRoomMapper {
    UserInChatRoom userToUserInChatRoom(User user);
}
