package OneCoin.Server.order.repository;

import OneCoin.Server.helper.StubData;
import OneCoin.Server.order.entity.RedisOrder;
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
    @DisplayName("@Indexed 어노테이션을 사용하면 해당 필드로 select 문이 실행된다.")
    void indexedTest() {
        // given
        List<RedisOrder> redisOrders = StubData.MockRedisOrder.getMockEntities();
        redisOrderRepository.saveAll(redisOrders);

        // when
        BigDecimal limit = new BigDecimal("333333");
        boolean askBid = true;
        String code = "KRW-ETH";
        List<RedisOrder> findRedisOrders = redisOrderRepository.findAllByLimitAndAskBidAndCode(limit, askBid, code);

        // then
        assertThat(findRedisOrders.size()).isEqualTo(2);
        assertThat(findRedisOrders.get(0).getOrderId()).isEqualTo(3L);
        assertThat(findRedisOrders.get(1).getOrderId()).isEqualTo(5L);
    }
}
