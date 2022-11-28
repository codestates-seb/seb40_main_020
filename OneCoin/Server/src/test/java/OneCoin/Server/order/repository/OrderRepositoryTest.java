package OneCoin.Server.order.repository;

import OneCoin.Server.helper.StubData;
import OneCoin.Server.order.entity.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataRedisTest
@MockBean(JpaMetamodelMappingContext.class)
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void saveEntity() {
        Order order = StubData.MockOrder.getMockEntity();
        orderRepository.save(order);
    }

    @AfterEach
    void deleteAll() {
        orderRepository.deleteAll();
    }

    @Test
    @DisplayName("Redis에 key-value 형태로 저장된다.")
    void redisConnectTest() {
        Order findOrder = orderRepository.findById(1L).orElseThrow();
        assertThat(findOrder.getOrderId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("@Indexed 어노테이션을 사용하면 해당 필드로 select 문이 실행된다.")
    void indexedTest() {
        // given
        List<Order> orders = StubData.MockOrder.getMockEntities();
        orderRepository.saveAll(orders);

        // when
        BigDecimal limit = new BigDecimal("333333");
        String orderType = "ASK";
        String code = "KRW-ETH";
        List<Order> findOrders = orderRepository.findAllByLimitAndOrderTypeAndCode(limit, orderType, code);

        // then
        assertThat(findOrders.size()).isEqualTo(2);
        assertThat(findOrders.get(0).getOrderId()).isEqualTo(3L);
        assertThat(findOrders.get(1).getOrderId()).isEqualTo(5L);
    }
}
