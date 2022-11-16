package OneCoin.Server.order.repository;

import OneCoin.Server.order.entity.AskOrder;
import org.springframework.data.repository.CrudRepository;

public interface OrderRedisRepository extends CrudRepository<AskOrder, Long> {
}
