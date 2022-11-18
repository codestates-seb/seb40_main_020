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
                    .amount(new BigDecimal("113"))
                    .askOrBid(true)
                    .coin(MockCoin.getMockEntity(1L, "KRW-BTC", "비트코인"))
                    .build();
        }

        public static RedisOrder getMockEntity(long orderId, String limit,
                                               String amount, boolean askOrBid,
                                               long coinId, String code, String coinName) {
            return RedisOrder
                    .builder()
                    .orderId(orderId)
                    .limit(new BigDecimal(limit))
                    .amount(new BigDecimal(amount))
                    .askOrBid(askOrBid)
                    .coin(MockCoin.getMockEntity(coinId, code, coinName))
                    .build();
        }
    }

    public static class MockRedisPostDto {
        public static RedisOrderDto.Post getMockRedisPost() {
            RedisOrderDto.Post redisPostDto = new RedisOrderDto.Post();
            redisPostDto.setLimit(12345000);
            redisPostDto.setAmount(66);
            redisPostDto.setAskOrBid(0);

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
