package OneCoin.Server.config.auth.utils;

import OneCoin.Server.user.entity.User;
import OneCoin.Server.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Map;

@Component
@AllArgsConstructor
public class LoggedInUserInfoUtilsForWebSocket {
    private UserService userService;

    public User extractUser(Principal principal) {
        Authentication authentication = (Authentication) principal;
        Map<String, Object> claims = (Map) authentication.getPrincipal();
        Long userId = ((Integer) claims.get("id")).longValue();
        User user = userService.findUser(userId);
        return user;
    }
}