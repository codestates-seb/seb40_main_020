package OneCoin.Server.config.auth.utils;

import OneCoin.Server.config.auth.jwt.JwtTokenizer;
import OneCoin.Server.user.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthorizationUtilsForWebSocket {
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils customAuthorityUtils;

    public void verifyAuthorization(StompHeaderAccessor accessor) {
        String authorizationValue = accessor.getFirstNativeHeader("Authorization");
        if (authorizationValue == null || authorizationValue.equals("null")) return;
        String jws = authorizationValue.replace("Bearer ", "");
        Map<String, Object> claims = verifyJws(jws);
        Authentication user = setAndGetAuthentication(claims);
        accessor.setUser(user);
        accessor.setLeaveMutable(true);
    }

    private Map<String, Object> verifyJws(String jws) {
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey()); // SecretKey 파싱
        Map<String, Object> claims = jwtTokenizer.getClaims(jws, base64EncodedSecretKey).getBody();   // Claims 파싱
        return claims;
    }

    private Authentication setAndGetAuthentication(Map<String, Object> claims) {
        List<GrantedAuthority> authorities = customAuthorityUtils.createAuthorities(Role.valueOf((String) claims.get("roles")));
        Authentication authentication = new UsernamePasswordAuthenticationToken(claims, null, authorities);
        return authentication;
    }
}
