package OneCoin.Server.order.repository;

import OneCoin.Server.order.entity.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRedisRepository extends CrudRepository<Order, Long> {
}
