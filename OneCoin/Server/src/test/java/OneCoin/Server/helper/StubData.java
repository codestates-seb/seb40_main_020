package OneCoin.Server.helper;

import OneCoin.Server.coin.entity.Coin;
import OneCoin.Server.order.dto.RedisOrderDto;
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
                    .market(new BigDecimal("0.0"))
                    .stopLimit(new BigDecimal("0.0"))
                    .amount(new BigDecimal("600"))
                    .coin(MockCoin.getMockEntity())
                    .build();
        }
    }

    public static class MockRedisPostDto {
        public static RedisOrderDto.Post getMockRedisPost() {
            RedisOrderDto.Post redisPostDto = new RedisOrderDto.Post();
            redisPostDto.setCode("KRW-BTC");
            redisPostDto.setLimit(12345000);
            redisPostDto.setAmount(66);
            redisPostDto.setAskOrBid(0);

            return redisPostDto;
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
