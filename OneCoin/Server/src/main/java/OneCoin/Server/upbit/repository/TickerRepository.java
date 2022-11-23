package OneCoin.Server.upbit.repository;

import OneCoin.Server.upbit.entity.Ticker;
import org.springframework.data.repository.CrudRepository;

public interface TickerRepository extends CrudRepository<Ticker, String> {
}
