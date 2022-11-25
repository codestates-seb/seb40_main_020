package OneCoin.Server.deposit.repository;

import OneCoin.Server.deposit.entity.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepositRepository extends JpaRepository<Deposit, Long> {
    List<Deposit> findByBalance_BalanceId(long balanceId);
}
