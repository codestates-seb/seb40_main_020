package OneCoin.Server.coin.repository;

import OneCoin.Server.coin.entity.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoinRepository extends JpaRepository<Coin, Long> {
    Optional<Coin> findByCode(String code);
}
