package OneCoin.Server.redis;

import OneCoin.Server.coin.entity.Coin;
import OneCoin.Server.coin.repository.CoinRepository;
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

    @Autowired
    private CoinRepository coinRepository;

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

    @Test
    @DisplayName("연관관계 매핑은 모든 필드를 key-value로 가진다.")
    void mappingTest() {
        // given
        RedisOrder redisOrder = new RedisOrder();
        redisOrder.setOrderId(1L);
        redisOrder.setLimit(new BigDecimal("20000"));
        redisOrder.setAmount(new BigDecimal("100"));
        redisOrder.setOrderTime(LocalDateTime.now());

        Coin coin = coinRepository.findById(1L).orElseThrow();
        redisOrder.setCoin(coin);

        // when
        redisOrderRepository.save(redisOrder);

        // then
        RedisOrder findRedisOrder = redisOrderRepository.findById(1L).orElseThrow();
        assertThat(findRedisOrder.getCoin().getCoinName()).isEqualTo("비트코인");
    }
}
