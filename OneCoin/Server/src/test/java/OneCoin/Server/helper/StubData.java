package OneCoin.Server.helper;

import OneCoin.Server.coin.entity.Coin;
import OneCoin.Server.order.dto.RedisOrderDto;
import OneCoin.Server.order.entity.RedisOrder;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.util.List;

public class StubData {

    public static class MockRedisOrder {
        public static RedisOrder getMockEntity() {
            return RedisOrder
                    .builder()
                    .orderId(1L)
                    .limit(new BigDecimal("333333"))
                    .amount(new BigDecimal("113"))
                    .askBid(true)
                    .coin(MockCoin.getMockEntity(1L, "KRW-BTC", "비트코인"))
                    .build();
        }

        public static RedisOrder getMockEntity(long orderId, String limit,
                                               String amount, boolean askBid,
                                               long coinId, String code, String coinName) {
            return RedisOrder
                    .builder()
                    .orderId(orderId)
                    .limit(new BigDecimal(limit))
                    .amount(new BigDecimal(amount))
                    .askBid(askBid)
                    .coin(MockCoin.getMockEntity(coinId, code, coinName))
                    .build();
        }

        public static List<RedisOrder> getMockEntities() {
            RedisOrder mockEntity1 = MockRedisOrder.getMockEntity(2L, "333333", "100", false, 1L, "KRW-BTC", "비트코인");
            RedisOrder mockEntity2 = MockRedisOrder.getMockEntity(3L, "333333", "200", true, 2L, "KRW-ETH", "이더리움");
            RedisOrder mockEntity3 = MockRedisOrder.getMockEntity(4L, "555555", "300", true, 2L,"KRW-ETH", "이더리움");
            RedisOrder mockEntity4 = MockRedisOrder.getMockEntity(5L, "555555", "400", false, 3L,"KRW-XRP", "리플");

            return List.of(mockEntity1, mockEntity2, mockEntity3, mockEntity4);
        }
    }

    public static class MockRedisPostDto {
        public static RedisOrderDto.Post getMockRedisPost() {
            RedisOrderDto.Post redisPostDto = new RedisOrderDto.Post();
            redisPostDto.setLimit(12345000);
            redisPostDto.setAmount(66);
            redisPostDto.setAskBid(0);

            return redisPostDto;
        }

    }

    public static class MockCoin {
        @SneakyThrows
        public static Coin getMockEntity(long coinId, String code, String coinName) {
            Constructor<Coin> constructor = Coin.class.getDeclaredConstructor(Long.class, String.class, String.class);
            constructor.setAccessible(true);
            Coin coin = constructor.newInstance(coinId, code, coinName);

            return coin;
        }
    }
}
