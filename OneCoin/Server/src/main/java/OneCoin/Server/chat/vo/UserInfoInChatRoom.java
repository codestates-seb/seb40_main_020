package OneCoin.Server.chat.vo;

import OneCoin.Server.chat.entity.UserInChatRoom;
import OneCoin.Server.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
public class UserInfoInChatRoom {
    @Setter
    private Integer chatRoomId;
    private User user;

    public void setUser(UserInChatRoom user) {
        if(user == null) return;
        this.user = User.builder()
                .displayName(user.getDisplayName())
                .email(user.getEmail())
                .build();
    }
}
