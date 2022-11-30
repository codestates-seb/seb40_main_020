package OneCoin.Server.chat.dto;

import OneCoin.Server.chat.entity.UserInChatRoom;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UsersInRoomResponseDto {
    private Integer chatRoomId;
    private List<UserInChatRoom> displayNames;

    public UsersInRoomResponseDto(Integer chatRoomId, List<UserInChatRoom> displayNames) {
        this.chatRoomId = chatRoomId;
        this.displayNames = displayNames;
    }
}
