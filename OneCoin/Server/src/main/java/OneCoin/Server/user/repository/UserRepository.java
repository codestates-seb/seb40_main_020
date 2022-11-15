package OneCoin.Server.user.repository;


import OneCoin.Server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    /** 이메일로 유저 검색 */
    Optional<User> findByEmail(String email);

    /** 이메일로 계정 존재 여부 검색 */
    Boolean existsByEmail(String email);
}
