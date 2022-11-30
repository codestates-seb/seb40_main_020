package OneCoin.Server.chat.utils;

import OneCoin.Server.config.auth.jwt.JwtTokenizer;
import OneCoin.Server.config.auth.utils.CustomAuthorityUtils;
import OneCoin.Server.user.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthUtilForWebsocket {
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils customAuthorityUtils;
    public Authentication authenticate(String accessToken) {
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
//        SecurityContextHolder.getContext().setAuthentication(authentication); // SecurityContext 에 Authentication 저장
        return authentication;
    }

}
