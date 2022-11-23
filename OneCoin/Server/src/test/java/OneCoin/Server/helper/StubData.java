package OneCoin.Server.helper;

import OneCoin.Server.coin.entity.Coin;
import OneCoin.Server.order.dto.OrderDto;
import OneCoin.Server.order.entity.Order;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.util.List;

public class StubData {

    public static class MockRedisOrder {
        public static Order getMockEntity() {
            return Order
                    .builder()
                    .orderId(1L)
                    .limit(new BigDecimal("333333"))
                    .amount(new BigDecimal("113"))
                    .askBid(true)
                    .code("KRW-BTC")
                    .build();
        }

        public static Order getMockEntity(long orderId, String limit,
                                          String amount, boolean askBid, String code) {
            return Order
                    .builder()
                    .orderId(orderId)
                    .limit(new BigDecimal(limit))
                    .amount(new BigDecimal(amount))
                    .askBid(askBid)
                    .code(code)
                    .build();
        }

        public static List<Order> getMockEntities() {
            Order mockEntity1 = MockRedisOrder.getMockEntity(2L, "333333", "100", false, "KRW-BTC");
            Order mockEntity2 = MockRedisOrder.getMockEntity(3L, "333333", "200", true, "KRW-ETH");
            Order mockEntity3 = MockRedisOrder.getMockEntity(4L, "555555", "300", true, "KRW-ETH");
            Order mockEntity4 = MockRedisOrder.getMockEntity(5L, "333333", "400", true, "KRW-ETH");
            Order mockEntity5 = MockRedisOrder.getMockEntity(6L, "555555", "500", false,"KRW-XRP");

            return List.of(mockEntity1, mockEntity2, mockEntity3, mockEntity4, mockEntity5);
        }
    }

    public static class MockRedisPostDto {
        public static OrderDto.Post getMockRedisPost() {
            OrderDto.Post redisPostDto = new OrderDto.Post();
            redisPostDto.setLimit(12345000);
            redisPostDto.setAmount(66);
            redisPostDto.setAskBid(0);

            return redisPostDto;
        }

    }

    public static class MockCoin {
        @SneakyThrows
        public static Coin getMockEntity(long coinId, String code, String coinName) throws Exception {
            Constructor<Coin> constructor = Coin.class.getDeclaredConstructor(Long.class, String.class, String.class);
            constructor.setAccessible(true);
            Coin coin = constructor.newInstance(coinId, code, coinName);

            return coin;
        }
    }
}
