package OneCoin.Server.chat.chatRoom.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersInRoomResponseDto {
    private Long chatRoomId;
    private List<UserInfo> userInfos = new ArrayList<>();
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInfo {
        private String displayName;
        private String email;
    }

    public void addUserInfo(UserInfo userInfo) {
        this.userInfos.add(userInfo);
    }
}
