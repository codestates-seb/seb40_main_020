package OneCoin.Server.redis.redisOrder;

import OneCoin.Server.helper.StubData;
import OneCoin.Server.order.entity.RedisOrder;
import OneCoin.Server.order.repository.RedisOrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataRedisTest
@MockBean(JpaMetamodelMappingContext.class)
public class RedisOrderRepositoryTest {

    @Autowired
    private RedisOrderRepository redisOrderRepository;

    @BeforeEach
    void saveEntity() {
        RedisOrder redisOrder = StubData.MockRedisOrder.getMockEntity();
        redisOrderRepository.save(redisOrder);
    }

    @AfterEach
    void deleteAll() {
        redisOrderRepository.deleteAll();
    }

    @Test
    @DisplayName("List 타입으로 반환한다.")
    void findAllTest() {
        List<RedisOrder> redisOrders = redisOrderRepository.findAll();

        assertThat("비트코인").isEqualTo(redisOrders.get(0).getCoin().getCoinName());
    }
}
