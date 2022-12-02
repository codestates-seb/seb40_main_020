package OneCoin.Server.order.service;

import OneCoin.Server.balance.service.BalanceService;
import OneCoin.Server.coin.service.CoinService;
import OneCoin.Server.config.auth.utils.LoggedInUserInfoUtils;
import OneCoin.Server.exception.BusinessLogicException;
import OneCoin.Server.helper.StubData;
import OneCoin.Server.order.entity.Order;
import OneCoin.Server.order.repository.OrderRepository;
import OneCoin.Server.order.repository.WalletRepository;
import OneCoin.Server.user.entity.User;
import OneCoin.Server.utils.CalculationUtil;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
@MockBean(OkHttpClient.class)
public class OrderServiceTest {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private WalletRepository walletRepository;
    @MockBean
    private BalanceService balanceService;
    @MockBean
    private LoggedInUserInfoUtils loggedInUserInfoUtils;
    @MockBean
    private CoinService coinService;
    @MockBean
    private TransactionHistoryService transactionHistoryService;
    @MockBean
    private CalculationUtil calculationUtil;
    private Order order = StubData.MockOrder.getMockEntity();
    private User user = StubData.MockUser.getMockEntity();

    @AfterEach
    void deleteAll() {
        orderRepository.deleteAll();
        walletRepository.deleteAll();
    }

    @Test
    @DisplayName("매수 주문을 저장한다.")
    void saveBidOrderTest() {
        // given
        given(loggedInUserInfoUtils.extractUser()).willReturn(user);
        doNothing().when(coinService).verifyCoinExists(anyString());
        doNothing().when(balanceService).updateBalanceByBid(anyLong(), any());
        String code = "KRW-BTC";

        // when
        orderService.createOrder(order, code);

        // then
        List<Order> findOrder = orderRepository.findAllByUserIdAndOrderTypeAndCode(user.getUserId(), "BID", code);
        assertThat(findOrder.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("매도 주문을 생성한다.")
    void saveAskOrderTest() {
        // given
        given(loggedInUserInfoUtils.extractUser()).willReturn(user);
        doNothing().when(coinService).verifyCoinExists(anyString());
        walletRepository.save(StubData.MockWallet.getMockEntity());
        order.setOrderType("ASK");
        order.setAmount(new BigDecimal("0.01"));
        String code = "KRW-BTC";

        // when
        orderService.createOrder(order, code);

        // then
        List<Order> findOrder = orderRepository.findAllByUserIdAndOrderTypeAndCode(user.getUserId(), "ASK", code);
        assertThat(findOrder.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("매도 시 wallet에 보유한 양보다 많이 주문하면 에러가 발생한다.")
    void askExceptionTest1() {
        // given
        given(loggedInUserInfoUtils.extractUser()).willReturn(user);
        doNothing().when(coinService).verifyCoinExists(anyString());
        walletRepository.save(StubData.MockWallet.getMockEntity());
        order.setOrderType("ASK");
        order.setAmount(new BigDecimal("11"));
        String code = "KRW-BTC";

        // when, then
        assertThrows(BusinessLogicException.class, () -> orderService.createOrder(order, code));
    }

    @Test
    @DisplayName("매도 시 wallet에 보유한 양과 이미 주문한 매도량을 더한 값보다 많이 주문하면 에러가 발생한다.")
    void askExceptionTest2() {
        // given
        given(loggedInUserInfoUtils.extractUser()).willReturn(user);
        doNothing().when(coinService).verifyCoinExists(anyString());
        walletRepository.save(StubData.MockWallet.getMockEntity());
        order.setOrderType("ASK");
        order.setAmount(new BigDecimal("10"));
        orderRepository.save(order);
        String code = "KRW-BTC";

        // when, then
        order.setAmount(new BigDecimal("1"));
        assertThrows(BusinessLogicException.class, () -> orderService.createOrder(order, code));
    }

    @Test
    @DisplayName("이전 매도 주문량을 가져온다")
    void getPrevAskOrderAmount() {
        // given
        order.setOrderType("ASK");
        order.setAmount(new BigDecimal("10"));
        orderRepository.save(order);

        // when
        BigDecimal prevAmount = orderService.getPrevAskOrderAmount(order.getUserId(), order.getCode());

        // then
        assertThat(prevAmount).isEqualTo(new BigDecimal("10"));
    }

    @Test
    @DisplayName("주문을 삭제한다")
    void cancelOrder() {
        // given
        orderRepository.save(order);
        given(loggedInUserInfoUtils.extractUser()).willReturn(user);
        given(calculationUtil.calculateByAddingCommission(any(), any())).willReturn(BigDecimal.ZERO);
        doNothing().when(balanceService).updateBalanceByAskOrCancelBid(anyLong(), any());
        doNothing().when(transactionHistoryService).createTransactionHistory(any());

        // when
        orderService.cancelOrder(1L);

        // then
        Order findOrder = orderRepository.findById(1L).orElse(null);
        assertThat(findOrder).isEqualTo(null);
    }
}
