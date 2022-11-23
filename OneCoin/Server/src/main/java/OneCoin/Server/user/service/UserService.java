package OneCoin.Server.user.service;

import OneCoin.Server.config.auth.utils.CustomAuthorityUtils;
import OneCoin.Server.exception.BusinessLogicException;
import OneCoin.Server.exception.ExceptionCode;
import OneCoin.Server.user.entity.User;
import OneCoin.Server.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
        User findUser = findVerifiedUser(user.getUserId());

        findUser.setDisplayName(user.getDisplayName());
        findUser.setEmail(user.getEmail());
        findUser.setPassword(passwordEncoder.encode(user.getPassword()));
        findUser.setBalance(user.getBalance());

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
     *     계정 존재 여부 조회
     *     이메일, 플랫폼이 동일한 계정이 있으면 true
     * </pre>
     */
    @Transactional
    public Boolean hasAccount(String email) {
        return userRepository.existsByEmail(email);
    }
}