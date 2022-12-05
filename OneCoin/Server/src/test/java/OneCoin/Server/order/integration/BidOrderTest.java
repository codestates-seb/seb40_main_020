package OneCoin.Server.order.integration;

import OneCoin.Server.balance.service.BalanceService;
import OneCoin.Server.coin.entity.Coin;
import OneCoin.Server.coin.repository.CoinRepository;
import OneCoin.Server.config.auth.utils.LoggedInUserInfoUtils;
import OneCoin.Server.deposit.entity.Deposit;
import OneCoin.Server.deposit.service.DepositService;
import OneCoin.Server.helper.StubData;
import OneCoin.Server.order.dto.OrderDto;
import OneCoin.Server.order.entity.Order;
import OneCoin.Server.order.mapper.OrderMapper;
import OneCoin.Server.order.repository.OrderRepository;
import OneCoin.Server.order.service.OrderService;
import OneCoin.Server.user.dto.UserDto;
import OneCoin.Server.user.entity.User;
import OneCoin.Server.user.mapper.UserMapper;
import OneCoin.Server.user.repository.UserRepository;
import OneCoin.Server.user.service.UserService;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@MockBean(OkHttpClient.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BidOrderTest {
    @Autowired
    private DepositService depositService;
    @Autowired
    private BalanceService balanceService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CoinRepository coinRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @MockBean
    private LoggedInUserInfoUtils loggedInUserInfoUtils;
    private User user;
    private Coin coin;

    @BeforeAll
    void createEntity() {
        coin = StubData.MockCoin.getMockEntity(1L, "KRW-BTC", "비트코인");
        coinRepository.save(coin);
        UserDto.Post userPostDto = new UserDto.Post("given", "given@gmail.com", "1q2w3e4r@");
        user = userMapper.userPostToUser(userPostDto);
        userService.createUser(user);

        Deposit deposit = new Deposit();
        deposit.setBalance(balanceService.findBalanceByUserId(1L));
        deposit.setDepositAmount(100_000_000L);
        depositService.createDeposit(deposit);
    }

    @Test
    @DisplayName("매수 주문 후 유저의 잔액을 확인한다.")
    void checkBalanceAfterBidOrder() {
        // given
        OrderDto.Post postDto = StubData.MockOrderPostDto.getMockOrderPost();
        postDto.setLimit("10000000");
        postDto.setAmount("5");
        Order order = orderMapper.postDtoToOrder(postDto);
        given(loggedInUserInfoUtils.extractUser()).willReturn(user);

        // when
        orderService.createOrder(order, "KRW-BTC");

        // then
        User findUser = userRepository.findById(1L).orElse(null);
        BigDecimal remainingBalance = findUser.getBalance().getBalance();
        assertThat(remainingBalance).isEqualTo(new BigDecimal("49975000.00"));
    }

    @Test
    @DisplayName("매수 취소 후 유저의 잔액을 다시 환불하는지 확인한다.")
    void checkBalanceAfterCancelBidOrder() {
        // given
        given(loggedInUserInfoUtils.extractUser()).willReturn(user);
        List<Order> orders = orderService.findOrders();
        Order order = orders.get(0);
        order.setCompletedAmount(new BigDecimal("1"));
        order.setAmount(new BigDecimal("4"));
        orderRepository.save(order);

        // when
        orderService.cancelOrder(order.getOrderId());

        // then
        User findUser = userRepository.findById(1L).orElse(null);
        BigDecimal remainingBalance = findUser.getBalance().getBalance();
        assertThat(remainingBalance).isEqualTo(new BigDecimal("89995000.00"));

        List<Order> nullOrders = (List<Order>) orderRepository.findAll();
        assertTrue(nullOrders.isEmpty());
    }
}
