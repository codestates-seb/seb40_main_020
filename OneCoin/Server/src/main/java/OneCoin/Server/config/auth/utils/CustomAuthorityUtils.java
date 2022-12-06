package OneCoin.Server.config.auth.utils;

import OneCoin.Server.user.entity.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <pre>
 *     권한 정보 생성, 저장
 * </pre>
 */
@Service
public class CustomAuthorityUtils {
    @Value("${mail.address.admin}")
    private String adminMailAddress;

    private final List<GrantedAuthority> ADMIN_ROLES = AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER", "ROLE_NOT_AUTH");
    private final List<GrantedAuthority> USER_ROLES = AuthorityUtils.createAuthorityList("ROLE_NOT_AUTH", "ROLE_USER");
    private final List<GrantedAuthority> NOT_AUTH_ROLES = AuthorityUtils.createAuthorityList("ROLE_NOT_AUTH");

    // 메모리 상의 Role 을 기반으로 권한 정보 생성
    public List<GrantedAuthority> createAuthorities(String email) {
        if (email.equals(adminMailAddress)) {
            return ADMIN_ROLES;
        }
        return NOT_AUTH_ROLES;
    }

    // DB에 저장된 Role 을 기반으로 권한 정보 생성
    public List<GrantedAuthority> createAuthorities(Role role) {
        if (role.equals(Role.ROLE_ADMIN)) {
            return ADMIN_ROLES;
        } else if (role.equals(Role.ROLE_USER)) {
            return USER_ROLES;
        } else return NOT_AUTH_ROLES;
    }

    // DB 저장 용
    public Role createRoles(String email) {
        if (email.equals(adminMailAddress)) {
            return Role.ROLE_ADMIN;
        }
        return Role.ROLE_NOT_AUTH;
    }

}
