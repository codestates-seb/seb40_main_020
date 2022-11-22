package OneCoin.Server.config.auth.utils;

import OneCoin.Server.exception.BusinessLogicException;
import OneCoin.Server.exception.ExceptionCode;
import OneCoin.Server.user.entity.User;
import OneCoin.Server.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Map;

@Component
@AllArgsConstructor
public class LoggedInUserInfoUtilsForWebSocket {
    private UserRepository userRepository;
    public User extractUser(Principal principal) {
        Authentication authentication = (Authentication) principal;
        Map<String, Object> claims = (Map) authentication.getPrincipal();
        Long userId = ((Integer) claims.get("id")).longValue();
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
        return user;
    }
}