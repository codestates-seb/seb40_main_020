package OneCoin.Server.helper;

import OneCoin.Server.coin.entity.Coin;
import OneCoin.Server.order.dto.OrderDto;
import OneCoin.Server.order.entity.Order;
import OneCoin.Server.order.entity.Wallet;
import OneCoin.Server.user.entity.Platform;
import OneCoin.Server.user.entity.Role;
import OneCoin.Server.user.entity.User;

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
                    .limit(new BigDecimal("22525000"))
                    .amount(new BigDecimal("10"))
                    .completedAmount(BigDecimal.ZERO)
                    .orderType("BID")
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
                    .completedAmount(BigDecimal.ZERO)
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

    public static class MockUpbitAPI {
        public static String getJsonTicker() {
            return "{" +
                    "\"type\":\"ticker\"," +
                    "\"code\":\"KRW-BTC\"," +
                    "\"high_price\":22587000," +
                    "\"low_price\":22155000," +
                    "\"trade_price\":22525000," +
                    "\"prev_closing_price\":2.2339E7," +
                    "\"change\":\"RISE\"," +
                    "\"change_price\":186000.0," +
                    "\"change_rate\":0.0083262456," +
                    "\"ask_bid\":\"BID\"," +
                    "\"trade_volume\":2.2237E-4," +
                    "\"trade_time\":\"071622\"," +
                    "\"timestamp\":1669706182202," +
                    "\"acc_trade_price_24h\":6.390681659003425E10," +
                    "\"acc_trade_volume_24h\":2854.74052566" +
                    "}";
        }

        public static String getJsonOrderBook() {
            return "{" +
                    "\"type\":\"orderbook\"," +
                    "\"code\":\"KRW-BTC\"," +
                    "\"total_ask_size\":5.365745690000001," +
                    "\"total_bid_size\":13.740592939999999," +
                    "\"orderbook_units\":" +
                    "[" +
                    "{\"ask_price\":2.2525E7,\"bid_price\":2.2521E7,\"ask_size\":7.83E-5,\"bid_size\":0.18899245}," +
                    "{\"ask_price\":2.2529E7,\"bid_price\":2.252E7,\"ask_size\":0.4,\"bid_size\":0.0017718}," +
                    "{\"ask_price\":2.253E7,\"bid_price\":2.2511E7,\"ask_size\":0.48605748,\"bid_size\":0.6392}," +
                    "{\"ask_price\":2.2532E7,\"bid_price\":2.251E7,\"ask_size\":0.31829486,\"bid_size\":0.6}," +
                    "{\"ask_price\":2.2533E7,\"bid_price\":2.2509E7,\"ask_size\":0.03062177,\"bid_size\":0.58319307}," +
                    "{\"ask_price\":2.2534E7,\"bid_price\":2.2507E7,\"ask_size\":4.5431E-4,\"bid_size\":0.00223437}," +
                    "{\"ask_price\":2.2535E7,\"bid_price\":2.2506E7,\"ask_size\":0.5291849,\"bid_size\":0.01780993}," +
                    "{\"ask_price\":2.2537E7,\"bid_price\":2.2505E7,\"ask_size\":0.36774815,\"bid_size\":0.05282096}," +
                    "{\"ask_price\":2.2538E7,\"bid_price\":2.2504E7,\"ask_size\":0.375,\"bid_size\":0.17519573}," +
                    "{\"ask_price\":2.2539E7,\"bid_price\":2.2503E7,\"ask_size\":2.9253E-4,\"bid_size\":0.13094987}," +
                    "{\"ask_price\":2.254E7,\"bid_price\":2.2502E7,\"ask_size\":0.04893041,\"bid_size\":0.18022975}," +
                    "{\"ask_price\":2.2541E7,\"bid_price\":2.25E7,\"ask_size\":0.60760042,\"bid_size\":1.2637169}," +
                    "{\"ask_price\":2.2542E7,\"bid_price\":2.2499E7,\"ask_size\":0.03392823,\"bid_size\":0.01782062}," +
                    "{\"ask_price\":2.2543E7,\"bid_price\":2.2498E7,\"ask_size\":1.84489573,\"bid_size\":2.32341126}," +
                    "{\"ask_price\":2.2544E7,\"bid_price\":2.2497E7,\"ask_size\":0.3226586,\"bid_size\":7.56324623}" +
                    "]" +
                    "}";

        }
    }

    public static class MockWallet {
        public static Wallet getMockEntity() {
            return Wallet
                    .builder()
                    .amount(new BigDecimal("1"))
                    .averagePrice(new BigDecimal("22523000"))
                    .userId(1L)
                    .code("KRW-BTC")
                    .build();
        }
    }

    public static class MockUser {
        public static User getMockEntity() {
            return User.builder()
                    .userId(1L)
                    .userRole(Role.ROLE_USER)
                    .displayName("cococo")
                    .email("cococo@google.com")
                    .password("1q2w3e4r!@")
                    .platform(Platform.KAKAO)
                    .build();
        }
    }
}
