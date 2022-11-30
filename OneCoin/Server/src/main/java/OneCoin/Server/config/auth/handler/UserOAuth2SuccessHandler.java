package OneCoin.Server.config.auth.handler;

import OneCoin.Server.config.auth.jwt.JwtTokenizer;
import OneCoin.Server.config.auth.utils.CustomAuthorityUtils;
import OneCoin.Server.user.entity.User;
import OneCoin.Server.user.mapper.UserMapper;
import OneCoin.Server.user.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

public class UserOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils customAuthorityUtils;
    private final UserService userService;
    private final UserMapper userMapper;

    public UserOAuth2SuccessHandler(JwtTokenizer jwtTokenizer, CustomAuthorityUtils customAuthorityUtils, UserService userService, UserMapper userMapper) {
        this.jwtTokenizer = jwtTokenizer;
        this.customAuthorityUtils = customAuthorityUtils;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();

        System.out.println(oAuth2User.getName().toString());

        User user = userMapper.oAuth2UserToUser(oAuth2User);

        // 계정 생성 및 불러오기
        User createdUser = userService.createOAuth2User(user);

        redirect(request, response, createdUser);
    }

    /**
     *  소셜로그인 전용 redirect
     */
    public void redirect(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        String accessToken = userService.delegateAccessToken(user, jwtTokenizer);
        String refreshToken = userService.delegateRefreshToken(user, jwtTokenizer);

        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("Refresh", refreshToken);

        String uri = createURI().toString();
        getRedirectStrategy().sendRedirect(request, response, uri);
    }

    /**
     *  소셜로그인 전용 redirect URI
     */
    public URI createURI() {
        return UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host("localhost")  // 프론트의 ip
                .path("/receive-token.html")
                .build()
                .toUri();
    }
}
