package OneCoin.Server.order;

import OneCoin.Server.helper.StubData;
import OneCoin.Server.order.entity.RedisOrder;
import OneCoin.Server.order.repository.RedisOrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RedisConnectTest {

    @Autowired
    private RedisOrderRepository redisOrderRepository;

    @AfterEach
    void clear() {
        redisOrderRepository.deleteAll();
    }

    @Test
    @DisplayName("Redis에 key-value 형태로 저장된다.")
    void redisConnect() {
        // given
        RedisOrder redisOrder = StubData.MockRedisOrder.getMockEntity();

        // when
        redisOrderRepository.save(redisOrder);

        // then
        RedisOrder findRedisOrder = redisOrderRepository.findById(1L).orElseThrow();
        assertThat(findRedisOrder.getOrderId()).isEqualTo(1L);
    }
}
