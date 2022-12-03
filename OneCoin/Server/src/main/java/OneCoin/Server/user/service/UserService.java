package OneCoin.Server.user.service;

import OneCoin.Server.config.auth.jwt.JwtTokenizer;
import OneCoin.Server.config.auth.utils.CustomAuthorityUtils;
import OneCoin.Server.exception.BusinessLogicException;
import OneCoin.Server.exception.ExceptionCode;
import OneCoin.Server.user.dto.UserDto;
import OneCoin.Server.user.entity.Auth;
import OneCoin.Server.user.entity.Platform;
import OneCoin.Server.user.entity.Role;
import OneCoin.Server.user.entity.User;
import OneCoin.Server.user.repository.UserRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Transactional
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();     // 순환호출 제거를 위해 생성
    private final CustomAuthorityUtils customAuthorityUtils;
    private final JavaMailSender javaMailSender;
    private final AuthService authService;

    @Value("${spring.server.ip}")
    private String ipAddress;
    @Value("${spring.mail.username}")
    private String authEmail;

    public UserService(UserRepository userRepository, CustomAuthorityUtils customAuthorityUtils, JavaMailSender javaMailSender, AuthService authService) {
        this.userRepository = userRepository;
        this.customAuthorityUtils = customAuthorityUtils;
        this.javaMailSender = javaMailSender;
        this.authService = authService;
    }

    /**
     * 유저 생성
     */
    @Transactional
    public User createUser(User user) {
        // 계정 존재 여부 조회
        if (!hasAccount(user.getEmail())) {
            //패스워드 암호화
            String encryptedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);

            // email 로 role 생성
            user.setUserRole(customAuthorityUtils.createRoles(user.getEmail()));

            // 계정 생성
            return userRepository.save(user);
        }

        return null;
    }

    /**
     * <pre>
     * 소셜로그인 유저 생성
     */
    @Transactional
    public User createOAuth2User(User user) {
        // 계정 존재 여부 조회
        if (!hasAccount(user.getEmail())) {
            //패스워드 암호화
            String encryptedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);

            // 계정 생성
            return userRepository.save(user);
        }

        // 계정(이메일)이 존재할 경우
        else {
            User findUser = findVerifiedUserByEmail(user.getEmail());

            // 플랫폼이 카카오면 해당 계정 반환
            if (findUser.getPlatform().equals(Platform.KAKAO)) {
                return findUser;
            }
            // 다른 플랫폼이면 에러
            throw new BusinessLogicException(ExceptionCode.USER_EXISTS);
        }

    }

    /**
     *  <pre>
     *      회원가입 이메일 인증 발송
     *  </pre>
     */
    @Transactional
    @Async("sendEmailExecutor")
    public void authenticationEmail(User user) {
        // 임시 비밀번호 + Auth 생성
        Auth auth = authService.createAuth(findVerifiedUserByEmail(user.getEmail()));
        String randomPassword = auth.getAuthPassword();

        // 인증 링크
        String link = "http://" + ipAddress + "/api/users/authentication-email/signup/"
                + auth.getAuthId().toString() + "/" + randomPassword;

        sendEmail(user, link);
    }

    /**
     * <pre>
     *      비밀번호 재설정 이메일 인증 발송
     *  </pre>
     */
    @Transactional
    @Async("sendEmailExecutor")
    public void authenticationEmailForPassword(User user) {
        // 임시 비밀번호 + Auth 생성
        Auth auth = authService.createAuth(findVerifiedUserByEmail(user.getEmail()));
        String randomPassword = auth.getAuthPassword();

        // 인증 링크
        String link = "http://" + ipAddress + "/api/users/authentication-email/password/"
                + auth.getAuthId().toString() + "/" + randomPassword;

        sendEmail(user, link);
    }

    /**
     * <pre>
     *      link 를 연결해주는 이메일 인증 발송
     *  </pre>
     */
    @Transactional
    public void sendEmail(User user, String link) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            // 메세지 바디 설정
            StringBuilder body = new StringBuilder();
            body.append("<html> <body><h1>OneCoin 이메일 인증</h1>");
            body.append("<div>다음 링크로 이동하여 인증을 완료하십시오.</div>");
            body.append("<div><a href=\"" + link + "\" target=\"_blank\">인증 완료하기</a></div> </body></html>");

            // MimeMessageHelper 설정
            mimeMessageHelper.setFrom(authEmail);    // 보내는 사람
            mimeMessageHelper.setTo(user.getEmail());   // 받는 사람
            mimeMessageHelper.setSubject("OneCoin 이메일 인증");     // 제목
            mimeMessageHelper.setText(body.toString(), true);       // 내용, html true

            // 전송
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <pre>
     *      회원가입 이메일 인증 후처리
     *      이메일 인증 링크 타고 오면 임시 발급 인증번호 대조 후 계정 활성화
     *  </pre>
     */
    @Transactional
    public void confirmEmail(long authId, String password) {
        Auth auth = authService.findVerifiedAuth(authId);
        String dbPassword = auth.getAuthPassword();

        // password 값 비교
        if (password.equals(dbPassword)) {
            // 맟으면 계정 활성화
            User user = findUser(auth.getUser().getUserId());

            user.setUserRole(Role.ROLE_USER);

            authService.deleteAuth(authId);
            userRepository.save(user);
        }

    }

    /**
     * <pre>
     *      비밀번호 변경 이메일 인증 후처리
     *      이메일 인증 링크 타고 오면 임시 발급 인증번호 토큰 생성하여 전달
     *  </pre>
     */
    @Transactional
    public User confirmEmailByPassword(long authId, String password) {
        Auth auth = authService.findVerifiedAuth(authId);
        String dbPassword = auth.getAuthPassword();

        // password 값 비교
        if (password.equals(dbPassword)) {
            User user = findUser(auth.getUser().getUserId());

            authService.deleteAuth(authId);
            return userRepository.save(user);
        }
        return null;
    }

    /**
     * <pre>
     *     회원 정보 변경
     *     displayName 만 변경 가능
     * </pre>
     */
    @Transactional
    public User updateUser(User user) {
        User findUser = findVerifiedUser(user.getUserId());

        findUser.setDisplayName(user.getDisplayName());

        return userRepository.save(findUser);
    }

    /**
     * <pre>
     *     비밀번호 재설정
     * </pre>
     */
    @Transactional
    public User resetPassword(User user) {
        User findUser = findVerifiedUser(user.getUserId());

        findUser.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(findUser);
    }

    /**
     * <pre>
     *     단일 회원 정보 가져오기
     * </pre>
     */
    @Transactional(readOnly = true)
    public User findUser(long userId) {
        return findVerifiedUser(userId);
    }

    /**
     * <pre>
     *     회원 정보 리스트 가져오기
     * </pre>
     */
    @Transactional(readOnly = true)
    public Page<User> findUsers(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size, Sort.by("userId")));
    }

    /**
     * <pre>
     *     닉네임 중복 체크 서비스
     *     닉네임이 동일한 계정이 있으면 에러
     * </pre>
     */
    @Transactional
    public Boolean checkDuplicateDisplayName(String displayName) {
        if (userRepository.existsByDisplayName(displayName)) {
            throw new BusinessLogicException(ExceptionCode.USER_EXISTS);
        }
        return false;
    }

    /**
     * <pre>
     *     이메일 중복 체크 서비스
     *     이메일이 동일한 계정이 있으면 에러
     * </pre>
     */
    @Transactional
    public Boolean checkDuplicateEmail(String email) {
        if (hasAccount(email)) {
            throw new BusinessLogicException(ExceptionCode.USER_EXISTS);
        }
        return false;
    }

    /**
     * <pre>
     *     회원 정보 삭제
     * </pre>
     */
    @Transactional
    public void deleteUser(long userId) {
        User findUser = findVerifiedUser(userId);
        userRepository.delete(findUser);
    }

    /**
     * <pre>
     *     userId로 단일 회원 정보 가져오기
     * </pre>
     */
    @Transactional(readOnly = true)
    public User findVerifiedUser(long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        User findUser = optionalUser.orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
        return findUser;
    }

    /**
     * <pre>
     *     이메일로 단일 회원 정보 가져오기
     * </pre>
     */
    @Transactional(readOnly = true)
    public User findVerifiedUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
    }

    /**
     * <pre>
     *     계정 존재 여부 조회
     *     이메일이 동일한 계정이 있으면 true
     * </pre>
     */
    @Transactional
    public Boolean hasAccount(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * AccessToken 생성 위임
     */
    public String delegateAccessToken(User user, JwtTokenizer jwtTokenizer) {
        // userInfo
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getUserId());
        claims.put("username", user.getEmail());
        claims.put("roles", user.getUserRole());
        claims.put("displayName", user.getDisplayName());

        String subject = user.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());

        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        return jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);
    }

    /**
     * RefreshToken 생성 위임
     */
    public String delegateRefreshToken(User user, JwtTokenizer jwtTokenizer) {
        // userInfo
        String subject = user.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes());

        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        return jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey);
    }

    /**
     *  플랫폼 구별
     */
    public UserDto.OAuth2Attribute of(String provider, String attributeKey,
                                      Map<String, Object> attributes) {
        if (provider.equals("kakao")) {
            return ofKakao("email", attributes);
        }
        else {
            throw new BusinessLogicException(ExceptionCode.UNDEFINED_PLATFORM);
        }
    }

    /**
     *  카카오 필요 어트리뷰트 추출
     */
    public UserDto.OAuth2Attribute ofKakao(String attributeKey,
                                            Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        UserDto.OAuth2Attribute attribute = new UserDto.OAuth2Attribute();

        attribute.setId(attributes.get("id").toString());
        attribute.setName((String) kakaoProfile.get("nickname"));
        attribute.setEmail((String) kakaoAccount.get("email"));
        attribute.setImagePath((String)kakaoProfile.get("profile_image_url"));
        attribute.setAttributes(kakaoAccount);
        attribute.setAttributeKey(attributeKey);

        return  attribute;
    }

    /**
     *  Attribute 를 Map 으로 매핑
     */
    public Map<String, Object> convertToMap(UserDto.OAuth2Attribute attribute) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", attribute.getId());
        map.put("key", attribute.getAttributeKey());
        map.put("name", attribute.getName());
        map.put("email", attribute.getEmail());
        map.put("picture", attribute.getImagePath());

        return map;
    }

    /**
     *  토큰에 있는 id랑 주소에 있는 id랑 동일한지(유효한 요청인지) 체크
     */
    public void isValidId(Long pathId, Long tokenId) {
        // 토큰 값이랑 userId 동일하지 않으면 인증 에러 발생
        if (pathId != tokenId) {
            throw new BusinessLogicException(ExceptionCode.NOT_VALID_AUTHENTICATION);
        }
    }

    /**
     *  프로필 사진 가져오기
     */
    @Transactional
    public byte[] pathToImage(String path) throws IOException {

        InputStream imageStream = new FileInputStream(path);
        byte[] imageByteArray = IOUtils.toByteArray(imageStream);
        imageStream.close();

        return imageByteArray;
    }
}