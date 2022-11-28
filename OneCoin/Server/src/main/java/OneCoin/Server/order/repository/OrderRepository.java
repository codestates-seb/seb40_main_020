package OneCoin.Server.order.repository;

import OneCoin.Server.order.entity.Order;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;
import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {

    List<Order> findAllByUserId(Long userId);

    List<Order> findAllByLimitAndOrderTypeAndCode(BigDecimal limit, String orderType, String code); // TODO option : 가격 limit, market, stop-limit
}
