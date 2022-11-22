package OneCoin.Server.config.auth.utils;

import OneCoin.Server.exception.BusinessLogicException;
import OneCoin.Server.exception.ExceptionCode;
import OneCoin.Server.user.entity.User;
import OneCoin.Server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class LoggedInUserInfoUtils {

    private final UserRepository userRepository;

    public Long extractUserId() {
        Map<?, ?> claims;
        try {
            claims = (Map<?, ?>) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e) {
            throw new BusinessLogicException(ExceptionCode.AUTHENTICATION_NOT_FOUND);
        }
        return ((Number)claims.get("id")).longValue();
    }

    public User extractUser() {
        long userId = extractUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
        return user;
    }
}
