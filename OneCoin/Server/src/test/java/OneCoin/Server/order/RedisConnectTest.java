package OneCoin.Server.order;

import OneCoin.Server.helper.StubData;
import OneCoin.Server.order.entity.Order;
import OneCoin.Server.order.repository.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RedisConnectTest {

    @Autowired
    private OrderRepository orderRepository;

    @AfterEach
    void clear() {
        orderRepository.deleteAll();
    }

    @Test
    @DisplayName("Redis에 key-value 형태로 저장된다.")
    void redisConnect() {
        // given
        Order order = StubData.MockOrder.getMockEntity();

        // when
        orderRepository.save(order);

        // then
        Order findOrder = orderRepository.findById(1L).orElseThrow();
        assertThat(findOrder.getOrderId()).isEqualTo(1L);
    }
}
