package OneCoin.Server.user.service;

import OneCoin.Server.user.entity.Auth;
import OneCoin.Server.user.entity.User;
import OneCoin.Server.user.repository.AuthRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Positive;
import java.util.Random;

@Transactional
@Service
public class AuthService {

    private final AuthRepository authRepository;

    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    /**
     * <pre>
     *     인증번호 생성 후 DB에 저장하고 전달
     *     UserService 에서 쓸 것
     * </pre>
     */
    @Transactional
    public String createAuth(User user) {
        String password = initRandomPassword(10);
        Auth auth = new Auth();

        auth.setAuthPassword(password);
        auth.setUser(user);

        authRepository.save(auth);

        return password;
    }

    /**
     * <pre>
     *     랜덤 인증번호 생성
     * </pre>
     */
    public String initRandomPassword(@Positive int size) {
        Random ran = new Random();
        StringBuffer sb = new StringBuffer();

        int num = 0;

        do {
            num = ran.nextInt(75) + 48;

            if((num >= 48 && num <= 57) || (num >= 65 && num <= 90) || (num >= 97 && num <= 122)) {
                sb.append((char)num);
            }else {
                continue;
            }
        } while (sb.length() < size);

        return sb.toString();
    }
}
