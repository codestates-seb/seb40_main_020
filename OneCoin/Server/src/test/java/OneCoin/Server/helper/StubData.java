package OneCoin.Server.helper;

import OneCoin.Server.coin.entity.Coin;
import OneCoin.Server.order.entity.RedisOrder;
import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;

public class StubData {

    public static class MockRedisOrder {

        public static RedisOrder getMockEntity() {
            return RedisOrder
                    .builder()
                    .orderId(1L)
                    .limit(new BigDecimal("333333"))
                    .amount(new BigDecimal("600"))
                    .coin(MockCoin.getMockEntity())
                    .build();
        }
    }

    public static class MockCoin {
        @SneakyThrows
        public static Coin getMockEntity() {
            Constructor<Coin> constructor = Coin.class.getDeclaredConstructor(Long.class, String.class, String.class);
            constructor.setAccessible(true);
            Coin coin = constructor.newInstance(1L, "KRW-BTC", "비트코인");

            return coin;
        }
    }
}
