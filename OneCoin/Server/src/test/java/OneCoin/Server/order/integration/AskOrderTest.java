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
import OneCoin.Server.order.entity.Wallet;
import OneCoin.Server.order.mapper.OrderMapper;
import OneCoin.Server.order.repository.OrderRepository;
import OneCoin.Server.order.service.OrderService;
import OneCoin.Server.order.service.WalletService;
import OneCoin.Server.upbit.entity.Trade;
import OneCoin.Server.upbit.service.TradingService;
import OneCoin.Server.user.dto.UserDto;
import OneCoin.Server.user.entity.User;
import OneCoin.Server.user.mapper.UserMapper;
import OneCoin.Server.user.repository.UserRepository;
import OneCoin.Server.user.service.UserService;
import OneCoin.Server.utils.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@MockBean(OkHttpClient.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AskOrderTest {
    @Autowired
    private DepositService depositService;
    @Autowired
    private BalanceService balanceService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private WalletService walletService;
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
    @Autowired
    private TradingService tradingService;
    @Autowired
    private JsonUtil jsonUtil;
    @Autowired
    private ObjectMapper objectMapper;
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
        deposit.setDepositAmount(0L);
        depositService.createDeposit(deposit);

        Order order = StubData.MockOrder.getMockEntity();
        order.setLimit(new BigDecimal("10000000"));
        order.setAmount(new BigDecimal("5"));
        walletService.createWallet(order, new BigDecimal("5"));
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    @DisplayName("매도 주문 시 정상적으로 주문이 들어가는지 확인한다.")
    void checkAskOrder() {
        // given
        OrderDto.Post postDto = StubData.MockOrderPostDto.getMockOrderPost();
        postDto.setLimit("10000000");
        postDto.setAmount("4");
        postDto.setOrderType("ASK");
        Order order = orderMapper.postDtoToOrder(postDto);
        given(loggedInUserInfoUtils.extractUser()).willReturn(user);

        // when, then
        assertDoesNotThrow(() -> orderService.createOrder(order, "KRW-BTC"));
        List<Order> orders = (List<Order>) orderRepository.findAll();
        assertThat(orders.get(0).getAmount()).isEqualTo(new BigDecimal("4"));
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    @DisplayName("매도 주문이 체결되고 유저의 잔액이 업데이트 된다.")
    void tradeAskOrder() throws JsonProcessingException {
        // given
        String jsonTrade = StubData.MockUpbitAPI.getJsonTrade();
        JsonNode jsonNodeTrade = jsonUtil.fromJson(jsonTrade, JsonNode.class);
        Trade trade = objectMapper.readValue(jsonNodeTrade.toString(), Trade.class);

        // when
        tradingService.completeOrders(trade);

        // then
        Wallet wallet = walletService.findMyWallet(1L, "KRW-BTC");
        assertThat(wallet.getAmount()).isEqualTo(BigDecimal.ONE);

        User findUser = userRepository.findById(1L).orElse(null);
        assertThat(findUser.getBalance().getBalance()).isEqualTo(new BigDecimal("39980000.00"));
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    @DisplayName("매도 주문이 전부 체결되면 미체결 주문이 삭제되며 wallet의 코인을 전부 매도하면 wallet도 삭제된다.")
    void tradeAskOrderAll() throws JsonProcessingException {
        // given
        OrderDto.Post postDto = StubData.MockOrderPostDto.getMockOrderPost();
        postDto.setLimit("10000000");
        postDto.setAmount("1");
        postDto.setOrderType("ASK");
        Order order = orderMapper.postDtoToOrder(postDto);
        given(loggedInUserInfoUtils.extractUser()).willReturn(user);
        orderService.createOrder(order, "KRW-BTC");

        String jsonTrade = StubData.MockUpbitAPI.getJsonTrade();
        JsonNode jsonNodeTrade = jsonUtil.fromJson(jsonTrade, JsonNode.class);
        Trade trade = objectMapper.readValue(jsonNodeTrade.toString(), Trade.class);

        // when
        tradingService.completeOrders(trade);

        // then
        assertTrue(orderRepository.findAllByUserId(1L).isEmpty());
        assertNull(walletService.findMyWallet(1L, "KRW-BTC"));
    }
}
