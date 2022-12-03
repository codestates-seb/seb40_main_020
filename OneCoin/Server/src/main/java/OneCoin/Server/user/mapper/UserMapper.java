package OneCoin.Server.user.mapper;

import OneCoin.Server.balance.entity.Balance;
import OneCoin.Server.user.dto.UserDto;
import OneCoin.Server.user.entity.Platform;
import OneCoin.Server.user.entity.Role;
import OneCoin.Server.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    default User userPostToUser(UserDto.Post requestBody) {
        User user = new User();

        // User 생성 시 Balance 도 같이 생성
        Balance balance = new Balance();

        user.setDisplayName(requestBody.getDisplayName());
        user.setEmail(requestBody.getEmail());
        user.setPassword(requestBody.getPassword());
        user.setPlatform(Platform.ORIGIN);
        user.setUserRole(Role.ROLE_NOT_AUTH);   // default : ROLE_NOT_AUTH
        user.setBalance(balance);
        user.setImagePath("/home/ec2-user/profile/default.jpg");    // default path

        balance.setBalance(BigDecimal.ZERO);
        balance.setUser(user);

        return user;
    }

    default User oAuth2UserToUser(OAuth2User requestBody) {
        Map<String, Object> attributes = requestBody.getAttributes();

        User user = new User();

        // User 생성 시 Balance 도 같이 생성
        Balance balance = new Balance();

        user.setDisplayName((String)attributes.get("id") + "@");
        user.setEmail((String) attributes.get("email"));
        user.setPassword((String) attributes.get("id") + "a!");    // kakao id 저장
        user.setImagePath((String) attributes.get("picture"));
        user.setPlatform(Platform.KAKAO);
        user.setUserRole(Role.ROLE_USER);
        user.setBalance(balance);

        balance.setBalance(BigDecimal.ZERO);
        balance.setUser(user);

        return user;
    }

    default User userPatchToUser(UserDto.Patch requestBody) {
        User user = new User();

        user.setDisplayName(requestBody.getDisplayName());
        user.setEmail("");
        user.setPassword("");
        user.setPlatform(Platform.ORIGIN);
        user.setUserRole(Role.ROLE_USER);

        return user;
    }

    default User userPasswordToUser(UserDto.Password requestBody) {
        User user = new User();

        user.setDisplayName("");
        user.setEmail("");
        user.setPassword(requestBody.getPassword());
        user.setPlatform(Platform.ORIGIN);
        user.setUserRole(Role.ROLE_USER);

        return user;
    }

    default UserDto.Response userToUserResponse(User user) {
        UserDto.Response response = new UserDto.Response();

        response.setId(user.getUserId());
        response.setEmail(user.getEmail());
        response.setDisplayName(user.getDisplayName());
        if (user.getPlatform().equals(Platform.ORIGIN)) {
            response.setImagePath("http://13.209.194.104:8080/api/users/images/" + user.getUserId());
        } else {
            response.setImagePath(user.getImagePath());
        }

        return response;
    }

    List<UserDto.Response> usersToUserResponses(List<User> users);
}
