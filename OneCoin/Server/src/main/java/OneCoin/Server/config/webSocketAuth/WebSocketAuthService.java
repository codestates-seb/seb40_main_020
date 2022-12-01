package OneCoin.Server.config.webSocketAuth;

import OneCoin.Server.config.auth.jwt.JwtTokenizer;
import OneCoin.Server.config.auth.utils.CustomAuthorityUtils;
import OneCoin.Server.config.auth.utils.UserUtilsForWebSocket;
import OneCoin.Server.user.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

//Todo: 테스트 필요
@Service
@RequiredArgsConstructor
public class WebSocketAuthService {
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils customAuthorityUtils;
    private final String AUTHORIZATION = "Authorization";

    public void authenticate(StompHeaderAccessor accessor) {
        String accessToken = accessor.getFirstNativeHeader(AUTHORIZATION);
        if (accessToken == null || accessToken.equals("") || accessToken.equals("null")) return;
        Authentication authentication = authenticate(accessToken);
        accessor.setUser(authentication);
    }

    private Authentication authenticate(String accessToken) {
        Map<String, Object> claims = verifyJws(accessToken);
        return getAuthentication(claims);
    }

    private Map<String, Object> verifyJws(String accessToken) {
        String jws = accessToken.replace("Bearer ", "");
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey()); // SecretKey 파싱
        Map<String, Object> claims = jwtTokenizer.getClaims(jws, base64EncodedSecretKey).getBody();   // Claims 파싱
        return claims;
    }

    private Authentication getAuthentication(Map<String, Object> claims) {
        List<GrantedAuthority> authorities = customAuthorityUtils.createAuthorities(Role.valueOf((String) claims.get("roles")));
        Authentication authentication = new UsernamePasswordAuthenticationToken(claims, null, authorities);
        return authentication;
    }
}
