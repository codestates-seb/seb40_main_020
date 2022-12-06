package OneCoin.Server.config.auth.filter;

import OneCoin.Server.config.auth.dto.LoginDto;
import OneCoin.Server.config.auth.jwt.JwtTokenizer;
import OneCoin.Server.user.entity.User;
import OneCoin.Server.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <pre>
 *     로그인 인증 EntryPoint
 *     UsernamePasswordAuthenticationFilter : 폼로그인 방식에서 사용하는 디폴트 Security Filter, 폼로그인이 아니더라도 Username/Password 기반의 인증을 처리하기 위해 UsernamePasswordAuthenticationFilter 를 확장해서 구현
 * </pre>
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;      // 로그인 인증 정보(Username/Password)를 전달 받아 UserDetailsService 와 인터랙션 한뒤 인증 여부를 판단
    private final JwtTokenizer jwtTokenizer;
    private final UserService userService;


    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenizer jwtTokenizer, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenizer = jwtTokenizer;
        this.userService = userService;
    }

    @SneakyThrows   // 명시적 예외처리 생략(사용시 확실하게 예외사항 없는지 유의 필수, 사용 지양)
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {

        ObjectMapper objectMapper = new ObjectMapper();
        LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class); // DTO 클래스로 역직렬화

        // UsernamePasswordAuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

        return authenticationManager.authenticate(authenticationToken);  // AuthenticationManager 에 인증 처리 위임
    }

    /**
     * <pre>
     *     인증에 성공할 경우 호출
     * </pre>
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws ServletException, IOException {
        User user = (User) authResult.getPrincipal();

        String accessToken = userService.delegateAccessToken(user, jwtTokenizer);
        String refreshToken = userService.delegateRefreshToken(user, jwtTokenizer);

        response.setHeader("Authorization", "Bearer " + accessToken);   // Bearer : JWT 나 Oauth 등 토큰 기반 인증방식 스키마
        response.setHeader("Refresh", refreshToken);

        this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);  // 성공 핸들러 실행
    }

}
