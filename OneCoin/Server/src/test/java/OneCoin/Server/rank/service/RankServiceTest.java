package OneCoin.Server.rank.service;

import OneCoin.Server.coin.entity.Coin;
import OneCoin.Server.coin.repository.CoinRepository;
import OneCoin.Server.helper.StubData;
import OneCoin.Server.order.entity.Order;
import OneCoin.Server.order.entity.TransactionHistory;
import OneCoin.Server.order.entity.Wallet;
import OneCoin.Server.order.entity.enums.TransactionType;
import OneCoin.Server.order.repository.OrderRepository;
import OneCoin.Server.order.repository.TransactionHistoryRepository;
import OneCoin.Server.order.repository.WalletRepository;
import OneCoin.Server.rank.dao.UserRoi;
import OneCoin.Server.upbit.dto.ticker.TickerDto;
import OneCoin.Server.upbit.repository.TickerRepository;
import OneCoin.Server.user.entity.User;
import OneCoin.Server.user.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RankServiceTest {
    @Autowired
    private RankService rankService;
    @Autowired
    private  WalletRepository walletRepository;
    @Autowired
    private TickerRepository tickerRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CoinRepository coinRepository;
    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;
    @Autowired
    private UserRepository userRepository;


    @BeforeAll
    void init() {
        //비트코인 0.03개를 주문을 걸었다.
        //비트코인 0.01개만 체결 됐다.
        //비트코인 0.01개는 20000000에 샀다. => 200000
        //이더리움 0.05개를 주문을 걸어 전부 체결 됐다. => 800000
        Wallet wallet1 = Wallet.builder()
                .amount(BigDecimal.valueOf(0.01))
                .averagePrice(BigDecimal.valueOf(10L))
                .userId(1L)
                .code("KRW-BTC")
                .build();
        Wallet wallet2 = Wallet.builder()
                .amount(BigDecimal.valueOf(0.05))
                .averagePrice(BigDecimal.valueOf(10L))
                .userId(1L)
                .code("KRW-ETH")
                .build();
        Wallet wallet3 = Wallet.builder()
                .amount(BigDecimal.valueOf(0.01))
                .averagePrice(BigDecimal.valueOf(10L))
                .userId(2L)
                .code("KRW-BTC")
                .build();
        walletRepository.save(wallet1);
        walletRepository.save(wallet2);
        walletRepository.save(wallet3);
        Coin coin1 = StubData.MockCoin.getMockEntity(1L, "KRW-BTC", "비트코인");
        Coin coin2 = StubData.MockCoin.getMockEntity(2L, "KRW-ETH", "이더리움");
        Coin coinSaved1 = coinRepository.save(coin1);
        Coin coinSaved2 = coinRepository.save(coin2);
        Order order = new Order();
        order.setAmount(BigDecimal.valueOf(0.02));
        order.setCompletedAmount(BigDecimal.valueOf(0.01));
        order.setCode("KRW-BTC");
        order.setLimit(BigDecimal.valueOf(20000000));
        order.setUserId(1L);
        orderRepository.save(order);
        User user = userRepository.findById(2L).get();

        TransactionHistory transactionHistory1 = StubData.MockHistory.getMockEntity(TransactionType.BID);
        transactionHistory1.setCoin(coinSaved2);
        TransactionHistory transactionHistory2 = StubData.MockHistory.getMockEntity(TransactionType.BID);
        transactionHistory2.setAmount(new BigDecimal("0.01"));
        transactionHistory2.setCoin(coinSaved1);
        transactionHistory2.setUser(user);
        transactionHistoryRepository.save(transactionHistory1);
        transactionHistoryRepository.save(transactionHistory2);
    }

    @AfterAll
    void deleteRedis() {
        walletRepository.deleteAll();
        orderRepository.deleteAll();
    }

    @Test
    void calculateRoiTest() {
        List<TickerDto> ts = tickerRepository.findTickers();
        List<UserRoi> userROIList = rankService.calculateAllRois();
        //then
        assertThat(userROIList.size()).isEqualTo(2);
    }

    @Test
    void getTop10Test() {
        List<UserRoi> top10 = rankService.calculateTop10();
        double firstRoiNum = top10.get(0).getTotalRoi();
        double secondRoiNum = top10.get(1).getTotalRoi();
        double diff = firstRoiNum - secondRoiNum;
        assertThat(diff).isPositive();
    }

}
