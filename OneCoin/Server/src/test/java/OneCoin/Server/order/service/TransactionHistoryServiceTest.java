package OneCoin.Server.order.service;

import OneCoin.Server.coin.entity.Coin;
import OneCoin.Server.coin.service.CoinService;
import OneCoin.Server.config.auth.utils.LoggedInUserInfoUtils;
import OneCoin.Server.exception.BusinessLogicException;
import OneCoin.Server.helper.StubData;
import OneCoin.Server.order.entity.Order;
import OneCoin.Server.order.entity.TransactionHistory;
import OneCoin.Server.order.entity.enums.TransactionType;
import OneCoin.Server.order.repository.TransactionHistoryRepository;
import OneCoin.Server.user.entity.User;
import OneCoin.Server.user.repository.UserRepository;
import OneCoin.Server.user.service.UserService;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@MockBean(OkHttpClient.class)
public class TransactionHistoryServiceTest {
    PageRequest pageRequest = PageRequest.of(0, 15, Sort.by("createdAt").descending());
    @Autowired
    private TransactionHistoryService transactionHistoryService;
    @Autowired
    private UserRepository userRepository;
    @MockBean
    private LoggedInUserInfoUtils loggedInUserInfoUtils;
    @MockBean
    private CoinService coinService;
    @MockBean
    private UserService userService;
    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;

    @BeforeEach
    void saveTransactionHistory() {
        userRepository.save(StubData.MockUser.getMockEntity());
        transactionHistoryRepository.save(StubData.MockHistory.getMockEntity());
    }

    @AfterEach
    void deleteAll() {
        transactionHistoryRepository.deleteAll();
    }

    @Test
    @DisplayName("거래 내역을 생성한다.")
    void createTransactionHistory() {
        // given
        Order order = StubData.MockOrder.getMockEntity();
        order.setAmount(BigDecimal.ZERO);
        order.setCompletedAmount(new BigDecimal("10"));
        User user = StubData.MockUser.getMockEntity();
        Coin coin = StubData.MockCoin.getMockEntity(1L, "KRW-BTC", "비트코인");
        given(userService.findVerifiedUser(anyLong())).willReturn(user);
        given(coinService.findCoin(anyString())).willReturn(coin);

        // when
        transactionHistoryService.createTransactionHistoryByOrder(order);

        // then
        List<TransactionHistory> transactionHistories = transactionHistoryRepository.findTop10ByUserAndCoinAndTransactionTypeOrTransactionTypeOrderByCreatedAtDesc(user, coin, TransactionType.BID, TransactionType.ASK);
        TransactionHistory transactionHistory = transactionHistories.get(0);
        assertThat(transactionHistory.getTransactionType()).isEqualTo(TransactionType.BID);
        assertThat(transactionHistory.getAmount()).isEqualTo(new BigDecimal("10.000000000000000"));
    }

    @ParameterizedTest
    @CsvSource(value = {"w:BID:KRW-BTC:1", "m:ASK::0", "3m:ALL:KRW-BTC:1", "6m:ALL::1"}, delimiter = ':')
    void searchTest(String period, String type, String code, int expectSize) {
        // given
        given(loggedInUserInfoUtils.extractUser()).willReturn(StubData.MockUser.getMockEntity());
        given(coinService.findCoin(anyString())).willReturn(StubData.MockCoin.getMockEntity(1L, code, "비트코인"));

        // when
        Page<TransactionHistory> transactionHistoryPage = transactionHistoryService.findTransactionHistory(period, type, code, pageRequest);
        List<TransactionHistory> transactionHistories = transactionHistoryPage.getContent();

        // then
        assertThat(transactionHistories.size()).isEqualTo(expectSize);
    }

    @Test
    @DisplayName("잘못된 기간을 보내면 에러가 발생한다")
    void periodExceptionTest() {
        // given
        String period = "a";

        // when then
        assertThrows(BusinessLogicException.class, () -> transactionHistoryService.findTransactionHistory(period, "BID", "KRW-BTC", pageRequest));
    }

    @Test
    @DisplayName("잘못된 타입을 입력하면 에러가 발생한다")
    void typeExceptionTest() {
        // given
        String type = "ABC";

        // when then
        assertThrows(BusinessLogicException.class, () -> transactionHistoryService.findTransactionHistory("w", type, "KRW-BTC", pageRequest));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ALL", "BID", "ASK", "DEPOSIT", "SWAP"})
    @DisplayName("지정한 타입만 메서드를 에러없이 실행한다")
    void typeTest(String type) {
        assertDoesNotThrow(() -> transactionHistoryService.findTransactionHistory("w", type, "KRW-BTC", pageRequest));
    }
}
