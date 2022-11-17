package OneCoin.Server.redis;

import OneCoin.Server.coin.entity.Coin;
import OneCoin.Server.coin.repository.CoinRepository;
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
        RedisOrder redisOrder = StubData.MockRedisOrder.getMockEntity();

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
        RedisOrder redisOrder = StubData.MockRedisOrder.getMockEntity();

        Coin coin = coinRepository.findById(2L).orElseThrow();
        redisOrder.setCoin(coin);

        // when
        redisOrderRepository.save(redisOrder);

        // then
        RedisOrder findRedisOrder = redisOrderRepository.findById(1L).orElseThrow();
        assertThat(findRedisOrder.getCoin().getCoinName()).isEqualTo("이더리움");
    }
}
