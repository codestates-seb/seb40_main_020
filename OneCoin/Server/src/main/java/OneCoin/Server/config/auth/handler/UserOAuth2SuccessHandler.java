package OneCoin.Server.config.auth.handler;

import OneCoin.Server.config.auth.jwt.JwtTokenizer;
import OneCoin.Server.config.auth.utils.CustomAuthorityUtils;
import OneCoin.Server.user.entity.User;
import OneCoin.Server.user.mapper.UserMapper;
import OneCoin.Server.user.service.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class UserOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils customAuthorityUtils;
    private final UserService userService;
    private final UserMapper userMapper;
    @Value("${spring.client.ip}")
    private String clientURL;

    public UserOAuth2SuccessHandler(JwtTokenizer jwtTokenizer, CustomAuthorityUtils customAuthorityUtils, UserService userService, UserMapper userMapper) {
        this.jwtTokenizer = jwtTokenizer;
        this.customAuthorityUtils = customAuthorityUtils;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @SneakyThrows
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();

        User user = userMapper.oAuth2UserToUser(oAuth2User);

        // 계정 생성 및 불러오기
        User createdUser = userService.createOAuth2User(user);

        redirect(request, response, createdUser);
    }

    /**
     *  소셜로그인 전용 redirect
     */
    public void redirect(HttpServletRequest request, HttpServletResponse response, User user) throws IOException, URISyntaxException {
        String accessToken = userService.delegateAccessToken(user, jwtTokenizer);
        String refreshToken = userService.delegateRefreshToken(user, jwtTokenizer);

        String uri = createURI(accessToken, refreshToken).toString();
        getRedirectStrategy().sendRedirect(request, response, uri);
    }

    /**
     *  소셜로그인 전용 redirect URI
     */
    public URI createURI(String accessToken, String refreshToken) throws URISyntaxException {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("authorization", accessToken);
        queryParams.add("refresh", refreshToken);

        return new URI("http://" + clientURL + "/token/password?authorization="
                + accessToken + "&refresh="
                + refreshToken);
    }
}
