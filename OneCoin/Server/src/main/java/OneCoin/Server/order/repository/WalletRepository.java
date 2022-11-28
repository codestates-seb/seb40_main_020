package OneCoin.Server.order.repository;

import OneCoin.Server.order.entity.Wallet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WalletRepository extends CrudRepository<Wallet, Long> {
    Wallet findByUserIdAndCode(long userId, String code);

    List<Wallet> findAllByUserId(long userId);
}
