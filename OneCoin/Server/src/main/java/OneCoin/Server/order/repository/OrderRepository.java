package OneCoin.Server.order.repository;

import OneCoin.Server.order.entity.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {
    List<Order> findAllByUserId(Long userId);

    List<Order> findAllByOrderTypeAndCode(String orderType, String code);

    List<Order> findAllByUserIdAndOrderTypeAndCode(Long userId, String orderType, String code);
}
