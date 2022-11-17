package OneCoin.Server.user.service;

import OneCoin.Server.config.auth.utils.CustomAuthorityUtils;
import OneCoin.Server.user.entity.User;
import OneCoin.Server.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthorityUtils customAuthorityUtils;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CustomAuthorityUtils customAuthorityUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.customAuthorityUtils = customAuthorityUtils;
    }

    /** 유저 생성 */
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

        // 기존 계정 반환
        return userRepository.findByEmail(user.getEmail()).orElseThrow();
    }

    /**
     * <pre>
     *     회원 정보 변경
     *     displayName, email, password, balance 만 변경 가능
     * </pre>
     */
    @Transactional
    public User updateUser(User user) {
        User findUser = userRepository.findById(user.getUserId()).orElseThrow();

        findUser.setDisplayName(user.getDisplayName());
        findUser.setEmail(user.getEmail());
        findUser.setPassword(passwordEncoder.encode(user.getPassword()));
        findUser.setBalance(user.getBalance());

        return userRepository.save(findUser);
    }

    /**
     * <pre>
     *     계정 존재 여부 조회
     *     이메일, 플랫폼이 동일한 계정이 있으면 true
     * </pre>
     */
    @Transactional
    public Boolean hasAccount(String email) {
        return userRepository.existsByEmail(email);
    }
}