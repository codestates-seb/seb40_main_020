package OneCoin.Server.chat.chatRoom.mapper;

import OneCoin.Server.chat.chatRoom.dto.UsersInRoomResponseDto;
import OneCoin.Server.chat.chatRoom.entity.UserInChatRoomInMemory;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatRoomMapper {

    default UsersInRoomResponseDto usersInRoomToResponseDto(List<UserInChatRoomInMemory> users) {
        UsersInRoomResponseDto response = new UsersInRoomResponseDto();
        if (users.size() > 0) {
            response.setChatRoomId(users.get(0).getChatRoomId());
        }
        users.stream().forEach(user -> {
            UsersInRoomResponseDto.UserInfo userInfo = new UsersInRoomResponseDto.UserInfo();
            userInfo.setDisplayName(user.getUserDisplayName());
            userInfo.setEmail(user.getUserEmail());
            response.addUserInfo(userInfo);
        });
        return response;
    }
}
