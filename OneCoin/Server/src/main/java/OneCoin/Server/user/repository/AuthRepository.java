package OneCoin.Server.user.repository;

import OneCoin.Server.user.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<Auth, Long> {
}
