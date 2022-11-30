package OneCoin.Server.order.repository;

import OneCoin.Server.order.entity.Wallet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface WalletRepository extends CrudRepository<Wallet, Long> {
    Optional<Wallet> findByUserIdAndCode(long userId, String code);

    List<Wallet> findAllByUserId(long userId);
}
