package OneCoin.Server.deposit.repository;

import OneCoin.Server.deposit.entity.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepositRepository extends JpaRepository<Deposit, Long> {
}
