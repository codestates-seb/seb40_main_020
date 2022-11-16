package OneCoin.Server.order.repository;

import OneCoin.Server.order.entity.RedisOrder;
import org.springframework.data.repository.CrudRepository;

public interface RedisOrderRepository extends CrudRepository<RedisOrder, Long> {
}
