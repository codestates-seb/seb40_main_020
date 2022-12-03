package OneCoin.Server.order.repository;

import OneCoin.Server.coin.entity.Coin;
import OneCoin.Server.coin.repository.CoinRepository;
import OneCoin.Server.helper.StubData;
import OneCoin.Server.order.entity.TransactionHistory;
import OneCoin.Server.order.entity.enums.TransactionType;
import OneCoin.Server.user.entity.User;
import OneCoin.Server.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@MockBean(JpaMetamodelMappingContext.class)
public class TransactionHistoryRepositoryTest {
    private final PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("createdAt").descending());
    private LocalDateTime searchPeriod = LocalDateTime.of(2022, Month.NOVEMBER, 28, 19, 22);
    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CoinRepository coinRepository;

    @Test
    @DisplayName("1주 이내 거래 내역을 거래된 시간 순으로 찾는다")
    void oneWeekTest() {
        // given
        User user = userRepository.findById(1L).orElse(null);
        searchPeriod = searchPeriod.minusWeeks(1);

        // when
        Page<TransactionHistory> transactionHistoryPage = transactionHistoryRepository.findByUserAndCreatedAtAfter(user, searchPeriod, pageRequest);

        // then
        List<TransactionHistory> transactionHistories = transactionHistoryPage.getContent();
        assertThat(transactionHistories.size()).isEqualTo(2);
        assertThat(transactionHistories.get(0).getTransactionHistoryId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("1달 이내 거래 내역 중 타입이 ASK인 내역을 거래된 시간 순으로 찾는다")
    void oneMonthAndTypeTest() {
        // given
        User user = userRepository.findById(1L).orElse(null);
        searchPeriod = searchPeriod.minusMonths(1);
        TransactionType transactionType = TransactionType.ASK;

        // when
        Page<TransactionHistory> transactionHistoryPage = transactionHistoryRepository.findByUserAndTransactionTypeAndCreatedAtAfter(user, transactionType, searchPeriod, pageRequest);

        // then
        List<TransactionHistory> transactionHistories = transactionHistoryPage.getContent();
        assertThat(transactionHistories.size()).isEqualTo(3);
        assertThat(transactionHistories.get(0).getTransactionType()).isEqualTo(transactionType);
    }

    @Test
    @DisplayName("3달 이내 거래 내역 중 이더리움을 검색한 내역을 거래된 시간 순으로 찾는다")
    void threeMonthsAndCoinTest() {
        // given
        User user = userRepository.findById(1L).orElse(null);
        searchPeriod = searchPeriod.minusMonths(3);
        Coin coin = coinRepository.findById(2L).orElse(null);

        // when
        Page<TransactionHistory> transactionHistoryPage = transactionHistoryRepository.findByUserAndCoinAndCreatedAtAfter(user, coin, searchPeriod, pageRequest);

        // then
        List<TransactionHistory> transactionHistories = transactionHistoryPage.getContent();
        assertThat(transactionHistories.size()).isEqualTo(3);
        assertThat(transactionHistories.get(0).getCoin().getCode()).isEqualTo(coin.getCode());
        assertThat(transactionHistories.get(0).getTransactionHistoryId()).isEqualTo(4);
    }

    @Test
    @DisplayName("6달 이내 거래 내역 중 타입이 BID이고 리플을 검색한 내역을 거래된 시간 순으로 찾는다")
    void sixMonthsAndCoinAndTypeTest() {
        // given
        User user = userRepository.findById(1L).orElse(null);
        searchPeriod = searchPeriod.minusMonths(6);
        Coin coin = coinRepository.findById(3L).orElse(null);
        TransactionType transactionType = TransactionType.BID;

        // when
        Page<TransactionHistory> transactionHistoryPage = transactionHistoryRepository.findByUserAndTransactionTypeAndCoinAndCreatedAtAfter(user, transactionType, coin, searchPeriod, pageRequest);

        // then
        List<TransactionHistory> transactionHistories = transactionHistoryPage.getContent();
        assertThat(transactionHistories.size()).isEqualTo(4);
        assertThat(transactionHistories.get(0).getCoin().getCode()).isEqualTo(coin.getCode());
        assertThat(transactionHistories.get(0).getTransactionHistoryId()).isEqualTo(8);
        assertThat(transactionHistories.get(3).getTransactionType()).isEqualTo(transactionType);
    }

    @Test
    @DisplayName("비트코인의 매수, 매도 체결 내역을 시간 순으로 찾는다")
    void findAllTest() {
        // given
        User user = StubData.MockUser.getMockEntity();
        Coin coin = StubData.MockCoin.getMockEntity(1L, "KRW-BTC", "비트코인");
        userRepository.save(user);
        coinRepository.save(coin);
        TransactionHistory transactionHistory = StubData.MockHistory.getMockEntity(TransactionType.BID);
        transactionHistoryRepository.save(transactionHistory);

        // when
        List<TransactionHistory> transactionHistories = transactionHistoryRepository.findTop10ByUserAndCoinAndTransactionTypeOrderByCreatedAtDesc(user, coin, TransactionType.BID);

        // then
        assertThat(transactionHistories.size()).isEqualTo(1);
        assertThat(transactionHistories.get(0).getCoin().getCode()).isEqualTo(coin.getCode());
        assertThat(transactionHistories.get(0).getTransactionHistoryId()).isEqualTo(1);
        assertThat(transactionHistories.get(0).getTransactionType()).isEqualTo(TransactionType.BID);
    }
}
