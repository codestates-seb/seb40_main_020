package OneCoin.Server.config.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * <pre>
 *     토큰 생성 서비스
 * </pre>
 */
@Service
public class JwtTokenizer {
    @Getter
    @Value("${jwt.secret-key}")
    private String secretKey;

    @Getter
    @Value("${jwt.access-token-expiration-minutes}")
    private int accessTokenExpirationMinutes;

    @Getter
    @Value("${jwt.refresh-token-expiration-minutes}")
    private int refreshTokenExpirationMinutes;

    public String encodeBase64SecretKey(String secretKey) {
        return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * <pre>
     *     AccessToken 생성
     * </pre>
     */
    public String generateAccessToken(Map<String, Object> claims,
                                      String subject,
                                      Date expiration,
                                      String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        return Jwts.builder()
                .setClaims(claims)          // 인증된 사용자와 관련된 정보를 추가
                .setSubject(subject)        // JWT 에 대한 제목 추가
                .setIssuedAt(Calendar.getInstance().getTime())      // JWT 발행일자를 설정 (java.util.Date)
                .setExpiration(expiration)      // JWT 의 만료일시를 지정
                .signWith(key)              // 서명을 위한 Key(java.security.Key) 객체 설정
                .compact();     //  JWT 생성하고 직렬화
    }

    /**
     * <pre>
     *     RefreshToken 생성
     * </pre>
     */
    public String generateRefreshToken(String subject, Date expiration, String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(Calendar.getInstance().getTime())
                .setExpiration(expiration)
                .signWith(key)
                .compact();
    }

    /**
     * <pre>
     *     JWT 에서 Claim 추출(JWT 검증 수행)
     *     jws : JWT + Signature
     * </pre>
     */
    public Jws<Claims> getClaims(String jws, String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        return Jwts.parserBuilder()
                .setSigningKey(key)     // 서명에 사용된 SecretKey 설정
                .build()
                .parseClaimsJws(jws);   // JWT 를 파싱해 Claim get
    }

    /**
     * <pre>
     *     JWT 검증(getClaims 에서 값을 가져오지 않고 검사만 수행)
     * </pre>
     */
    public void verifySignature(String jws, String base64EncodedSecretKey) {
        Key key = getKeyFromBase64EncodedKey(base64EncodedSecretKey);

        Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jws);
    }

    /**
     * <pre>
     *     토큰 만료일 지정
     * </pre>
     */
    public Date getTokenExpiration(int expirationMinutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, expirationMinutes);
        Date expiration = calendar.getTime();

        return expiration;
    }

    /**
     * <pre>
     *     인코딩 된 키를 가지고 HMAC 적용한 SecretKey 생성
     * </pre>
     */
    private Key getKeyFromBase64EncodedKey(String base64EncodedSecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);   // Secret 키 디코딩

        return Keys.hmacShaKeyFor(keyBytes);    // jjwt 에서 적절한 HMAC 알고리즘 자동 지정
    }
}
