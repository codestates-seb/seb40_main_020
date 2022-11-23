package OneCoin.Server.upbit.repository;

import OneCoin.Server.upbit.entity.OrderBook;
import org.springframework.data.repository.CrudRepository;

public interface OrderBookRepository extends CrudRepository<OrderBook, String> {
}
