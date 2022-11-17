package OneCoin.Server.user.mapper;

import OneCoin.Server.user.dto.UserDto;
import OneCoin.Server.user.entity.Platform;
import OneCoin.Server.user.entity.Role;
import OneCoin.Server.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    default User userPostToUser(UserDto.Post requestBody) {
        User user = new User();

        user.setDisplayName(requestBody.getDisplayName());
        user.setEmail(requestBody.getEmail());
        user.setPassword(requestBody.getPassword());
        user.setPlatform(Platform.ORIGIN);
        user.setBalance(0L);
        user.setUserRole(Role.ROLE_USER);   // default : ROLE_USER

        return user;
    }

    default User userPatchToUser(UserDto.Patch requestBody) {
        User user = new User();

        user.setDisplayName(requestBody.getDisplayName());
        user.setEmail(requestBody.getEmail());
        user.setPassword(requestBody.getPassword());
        user.setPlatform(Platform.ORIGIN);
        user.setBalance(0L);
        user.setUserRole(Role.ROLE_USER);

        return user;
    }

    default UserDto.Response userToUserResponse(User user) {
        UserDto.Response response = new UserDto.Response();

        response.setId(user.getUserId());
        response.setEmail(user.getEmail());
        response.setDisplayName(user.getDisplayName());

        return response;
    }

    List<UserDto.Response> usersToUserResponses(List<User> users);
}
