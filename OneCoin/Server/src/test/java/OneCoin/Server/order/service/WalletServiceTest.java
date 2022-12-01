package OneCoin.Server.order.service;

import OneCoin.Server.balance.service.BalanceService;
import OneCoin.Server.config.auth.utils.LoggedInUserInfoUtils;
import OneCoin.Server.helper.StubData;
import OneCoin.Server.order.entity.Order;
import OneCoin.Server.order.entity.Wallet;
import OneCoin.Server.order.repository.OrderRepository;
import OneCoin.Server.order.repository.WalletRepository;
import OneCoin.Server.user.entity.User;
import OneCoin.Server.user.repository.UserRepository;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@MockBean(OkHttpClient.class)
public class WalletServiceTest {

    @SpyBean
    private WalletService walletService;

    @MockBean
    private LoggedInUserInfoUtils loggedInUserInfoUtils;

    @MockBean
    private BalanceService balanceService;

    @MockBean
    private TransactionHistoryService transactionHistoryService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private UserRepository userRepository;

    private Order order;
    private User user;

    @BeforeEach
    void createOrder() {
        order = StubData.MockOrder.getMockEntity();
        orderRepository.save(order);
        user = StubData.MockUser.getMockEntity();
        userRepository.save(user);
    }

    @AfterEach
    void deleteAll() {
        orderRepository.deleteAll();
        walletRepository.deleteAll();
    }

    @Test
    @DisplayName("매수 체결 시 wallet 생성(일부 체결), order의 미체결량 감소")
    void createWallet1() {
        // given
        BigDecimal tradeVolume = new BigDecimal("1");

        // when
        walletService.createWallet(order, tradeVolume);

        // then
        Order findOrder = orderRepository.findById(1L).orElse(null);
        assertThat(findOrder.getCompletedAmount()).isEqualTo(tradeVolume);
        assertThat(findOrder.getAmount()).isEqualTo(new BigDecimal("9"));

        Wallet findWallet = walletRepository.findByUserIdAndCode(1L, "KRW-BTC").orElse(null);
        assertThat(findWallet.getAmount()).isEqualTo(tradeVolume);
    }

    @Test
    @DisplayName("매수 체결 시 wallet 생성(전체 체결), order 삭제")
    void createWallet2() {
        // given
        BigDecimal tradeVolume = new BigDecimal("10");

        // when
        doNothing().when(transactionHistoryService).createTransactionHistory(any());
        walletService.createWallet(order, tradeVolume);

        // then
        Order findOrder = orderRepository.findById(1L).orElse(null);
        assertThat(findOrder).isEqualTo(null);

        Wallet findWallet = walletRepository.findByUserIdAndCode(1L, "KRW-BTC").orElse(null);
        assertThat(findWallet.getAmount()).isEqualTo(tradeVolume);
    }

    @Test
    @DisplayName("매수 체결 시 wallet이 존재하면 업데이트")
    void updateWalletByBid() {
        // given
        Wallet wallet = StubData.MockWallet.getMockEntity();
        BigDecimal tradeVolume = new BigDecimal("10");

        // when
        doNothing().when(transactionHistoryService).createTransactionHistory(any());
        walletService.updateWalletByBid(wallet, order, tradeVolume);

        // then
        Wallet findWallet = walletRepository.findByUserIdAndCode(1L, "KRW-BTC").orElse(null);
        assertThat(findWallet.getAmount()).isEqualTo(new BigDecimal("11"));
        assertThat(findWallet.getAveragePrice()).isEqualTo(new BigDecimal("22524818.18"));
    }

    @Test
    @DisplayName("매도 체결 시 wallet 업데이트(일부 체결), wallet 보유량 감소")
    void updateWalletByAsk1() {
        // given
        Wallet wallet = StubData.MockWallet.getMockEntity();
        wallet.setAmount(new BigDecimal("10"));
        BigDecimal tradeVolume = new BigDecimal("9");

        // when
        doNothing().when(balanceService).updateBalanceByAskOrCancelBid(anyLong(), any());
        walletService.updateWalletByAsk(wallet, order, tradeVolume);

        // then
        Wallet findWallet = walletRepository.findByUserIdAndCode(1L, "KRW-BTC").orElse(null);
        assertThat(findWallet.getAmount()).isEqualTo(new BigDecimal("1"));
    }

    @Test
    @DisplayName("매도 체결 시 wallet 업데이트(전체 체결), wallet 삭제")
    void updateWalletByAsk2() {
        // given
        Wallet wallet = StubData.MockWallet.getMockEntity();
        wallet.setAmount(new BigDecimal("10"));
        walletRepository.save(wallet);
        BigDecimal tradeVolume = new BigDecimal("10");

        // when
        doNothing().when(balanceService).updateBalanceByAskOrCancelBid(anyLong(), any());
        wallet = walletRepository.findByUserIdAndCode(1L, "KRW-BTC").orElse(null);
        walletService.updateWalletByAsk(wallet, order, tradeVolume);

        // then
        Wallet findWallet = walletRepository.findByUserIdAndCode(1L, "KRW-BTC").orElse(null);
        assertThat(findWallet).isEqualTo(null);
    }

    @Test
    @DisplayName("user의 모든 코인 지갑을 찾는다.")
    void findUserWallets() {
        // given
        Wallet wallet1 = StubData.MockWallet.getMockEntity();
        walletRepository.save(wallet1);
        Wallet wallet2 = StubData.MockWallet.getMockEntity();
        wallet2.setCode("KRW-ETH");
        walletRepository.save(wallet2);

        // when
        when(loggedInUserInfoUtils.extractUser()).thenReturn(user);
        List<Wallet> wallets = walletService.findWallets();

        // then
        assertThat(wallets.size()).isEqualTo(2);
        assertThat(wallets.get(0).getCode()).isEqualTo("KRW-BTC");
        assertThat(wallets.get(1).getCode()).isEqualTo("KRW-ETH");
    }
}
