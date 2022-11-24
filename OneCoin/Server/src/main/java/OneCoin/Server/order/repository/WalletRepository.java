package OneCoin.Server.order.repository;

import OneCoin.Server.order.entity.Wallet;
import org.springframework.data.repository.CrudRepository;

public interface WalletRepository extends CrudRepository<Wallet, Long> {
}
