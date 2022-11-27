package OneCoin.Server.helper;

import OneCoin.Server.coin.entity.Coin;
import OneCoin.Server.order.dto.OrderDto;
import OneCoin.Server.order.entity.Order;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.List;

public class StubData {

    public static class MockOrder {
        public static Order getMockEntity() {
            return Order
                    .builder()
                    .orderId(1L)
                    .limit(new BigDecimal("333333"))
                    .amount(new BigDecimal("113"))
                    .orderType("ASK")
                    .code("KRW-BTC")
                    .userId(1L)
                    .build();
        }

        public static Order getMockEntity(long orderId, String limit,
                                          String amount, String orderType, String code, long userId) {
            return Order
                    .builder()
                    .orderId(orderId)
                    .limit(new BigDecimal(limit))
                    .amount(new BigDecimal(amount))
                    .orderType(orderType)
                    .code(code)
                    .userId(userId)
                    .build();
        }

        public static List<Order> getMockEntities() {
            Order mockEntity1 = MockOrder.getMockEntity(2L, "333333", "100", "BID", "KRW-BTC", 2L);
            Order mockEntity2 = MockOrder.getMockEntity(3L, "333333", "200", "ASK", "KRW-ETH", 3L);
            Order mockEntity3 = MockOrder.getMockEntity(4L, "555555", "300", "ASK", "KRW-ETH", 4L);
            Order mockEntity4 = MockOrder.getMockEntity(5L, "333333", "400", "ASK", "KRW-ETH", 5L);
            Order mockEntity5 = MockOrder.getMockEntity(6L, "555555", "500", "BID", "KRW-XRP", 6L);

            return List.of(mockEntity1, mockEntity2, mockEntity3, mockEntity4, mockEntity5);
        }
    }

    public static class MockOrderPostDto {
        public static OrderDto.Post getMockOrderPost() {
            OrderDto.Post redisPostDto = new OrderDto.Post();
            redisPostDto.setLimit(12345000);
            redisPostDto.setAmount(66);
            redisPostDto.setOrderType("BID");

            return redisPostDto;
        }

    }

    public static class MockCoin {
        public static Coin getMockEntity(long coinId, String code, String coinName) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
            Constructor<Coin> constructor = Coin.class.getDeclaredConstructor(Long.class, String.class, String.class);
            constructor.setAccessible(true);
            Coin coin = constructor.newInstance(coinId, code, coinName);

            return coin;
        }
    }
}
