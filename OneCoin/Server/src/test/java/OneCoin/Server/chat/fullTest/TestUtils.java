package OneCoin.Server.chat.fullTest;

import OneCoin.Server.config.auth.jwt.JwtTokenizer;
import OneCoin.Server.user.entity.Platform;
import OneCoin.Server.user.entity.Role;
import OneCoin.Server.user.entity.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestUtils {
    private final JwtTokenizer jwtTokenizer;

    public TestUtils(JwtTokenizer jwtTokenizer) {
        this.jwtTokenizer = jwtTokenizer;
    }

    public String generateAccessToken(User user) {
        // userInfo
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getUserId());
        claims.put("username", user.getEmail());
        claims.put("roles", user.getUserRole());

        String subject = user.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());

        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        return jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);
    }
    public User makeUser(String name) {
        return User.builder()
                .userRole(Role.ROLE_USER)
                .displayName(name)
                .email(name + "@google.com")
                .password(name + "1234!@")
                .platform(Platform.KAKAO)
                .build();
    }
}
