package OneCoin.Server.order.repository;

import OneCoin.Server.order.entity.RedisOrder;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RedisOrderRepository extends CrudRepository<RedisOrder, Long> {

    List<RedisOrder> findAll();
}
