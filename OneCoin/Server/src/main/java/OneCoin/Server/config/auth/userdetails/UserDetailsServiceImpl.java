package OneCoin.Server.config.auth.userdetails;

import OneCoin.Server.config.auth.utils.CustomAuthorityUtils;
import OneCoin.Server.exception.BusinessLogicException;
import OneCoin.Server.exception.ExceptionCode;
import OneCoin.Server.user.entity.Role;
import OneCoin.Server.user.entity.User;
import OneCoin.Server.user.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

/**
 * SpringSecurity 에서 User 관리할 수 있도록 하는 Service 단
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final CustomAuthorityUtils customAuthorityUtils;

    public UserDetailsServiceImpl(UserRepository userRepository, CustomAuthorityUtils customAuthorityUtils) {
        this.userRepository = userRepository;
        this.customAuthorityUtils = customAuthorityUtils;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(username);
        User findUser = optionalUser.orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        // 이메일 인증이 이루어지지 않은 계정이라면 로그인 못함
        if (findUser.getUserRole() == Role.ROLE_NOT_AUTH) {
            throw new BusinessLogicException(ExceptionCode.NO_AUTHENTICATION_EMAIL);
        }

        return new UserDetailsImpl(findUser);
    }

    private final class UserDetailsImpl extends User implements UserDetails {
        UserDetailsImpl(User user) {
            setUserId(user.getUserId());
            setEmail(user.getEmail());
            setPassword(user.getPassword());
            setUserRole(user.getUserRole());
            setDisplayName(user.getDisplayName());
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return customAuthorityUtils.createAuthorities(this.getUserRole());
        }

        // 주로 pk 넣지만 unique 인 이메일로 설정
        @Override
        public String getUsername() {
            return getEmail();
        }

        /**
         * 계정 만료 여부
         * true : 만료 안됨
         * false : 만료
         */
        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        /**
         * 계정 잠김 여부
         * true : 잠기지 않음
         * false : 잠김
         */
        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        /**
         * 크리덴셜 만료 여부
         * true : 만료 안됨
         * false : 만료
         */
        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        /**
         * 사용자 활성화 여부
         * ture : 활성화
         * false : 비활성화
         */
        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}
