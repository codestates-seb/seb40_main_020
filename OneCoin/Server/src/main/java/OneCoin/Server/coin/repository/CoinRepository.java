package OneCoin.Server.coin.repository;

import OneCoin.Server.coin.entity.Coin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinRepository extends JpaRepository<Coin, Long> {
}
