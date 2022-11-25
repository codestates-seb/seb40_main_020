package OneCoin.Server.balance.repository;

import OneCoin.Server.balance.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BalanceRepository extends JpaRepository<Balance, Long> {
    Optional<Balance> findByUser_UserId(Long userId);
}
