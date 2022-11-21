package OneCoin.Server.config;

import OneCoin.Server.config.auth.jwt.JwtTokenizer;
import OneCoin.Server.config.auth.utils.CustomAuthorityUtils;
import OneCoin.Server.user.entity.Role;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;

public class InterceptorForJwtAuthentication implements ChannelInterceptor {
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils customAuthorityUtils;

    public InterceptorForJwtAuthentication(JwtTokenizer jwtTokenizer, CustomAuthorityUtils customAuthorityUtils) {
        this.jwtTokenizer = jwtTokenizer;
        this.customAuthorityUtils = customAuthorityUtils;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authorizationValue = accessor.getFirstNativeHeader("Authorization");
            if (authorizationValue == null || authorizationValue.equals("null")) return message;
            String jws = authorizationValue.replace("Bearer ", "");
            Map<String, Object> claims = verifyJws(jws);
            Authentication user = setAndGetAuthentication(claims);
            accessor.setUser(user);
            accessor.setLeaveMutable(true);
        }
        return message;
    }
    private Map<String, Object> verifyJws(String jws) {
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey()); // SecretKey 파싱
        Map<String, Object> claims = jwtTokenizer.getClaims(jws, base64EncodedSecretKey).getBody();   // Claims 파싱
        return claims;
    }

    private Authentication setAndGetAuthentication(Map<String, Object> claims) {
        String username = (String) claims.get("username");
        List<GrantedAuthority> authorities = customAuthorityUtils.createAuthorities(Role.valueOf((String) claims.get("roles")));
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication); // SecurityContext 에 Authentication 저장
        return authentication;
    }
}
