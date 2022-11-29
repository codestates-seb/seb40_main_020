package OneCoin.Server.chat.chatRoom.mapper;

import OneCoin.Server.chat.chatRoom.entity.UserInChatRoom;
import OneCoin.Server.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatRoomMapper {
    UserInChatRoom userToUserInChatRoom(User user);
}
