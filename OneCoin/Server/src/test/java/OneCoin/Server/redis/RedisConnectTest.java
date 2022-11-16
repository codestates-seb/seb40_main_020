package OneCoin.Server.redis;

import OneCoin.Server.order.entity.RedisOrder;
import OneCoin.Server.order.repository.RedisOrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

//@DataRedisTest
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
        RedisOrder redisOrder = new RedisOrder();
        BigDecimal amount = new BigDecimal("100");
        BigDecimal limit = new BigDecimal("20000");
        redisOrder.setOrderId(1L);
        redisOrder.setAmount(amount);
        redisOrder.setLimit(limit);
        redisOrder.setOrderTime(LocalDateTime.now());

        // when
        redisOrderRepository.save(redisOrder);

        // then
        RedisOrder findRedisOrder = redisOrderRepository.findById(1L).orElseThrow();
        assertThat(findRedisOrder.getOrderId()).isEqualTo(1L);
    }
}