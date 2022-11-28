package OneCoin.Server.order.repository;

import OneCoin.Server.coin.entity.Coin;
import OneCoin.Server.coin.repository.CoinRepository;
import OneCoin.Server.order.entity.TransactionHistory;
import OneCoin.Server.order.entity.enums.TransactionType;
import OneCoin.Server.user.entity.Platform;
import OneCoin.Server.user.entity.User;
import OneCoin.Server.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class TransactionHistoryTest {
    @Autowired
    TransactionHistoryRepository transactionHistoryRepository;
    @Autowired
    CoinRepository coinRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    void queryTest() {
        User user = User.builder()
                .platform(Platform.KAKAO)
                .password("1234")
                .email("zoro@naver.com")
                .displayName("zoro")
                .build();
        User userSaved = userRepository.save(user);
        Coin coin = coinRepository.findById(1L).get();

        TransactionHistory history1 = TransactionHistory.builder()
                .transactionType(TransactionType.ASK)
                .amount(BigDecimal.valueOf(10L))
                .settledAmount(BigDecimal.valueOf(10010L))
                .user(userSaved)
                .coin(coin)
                .commission(10L)
                .orderTime(LocalDateTime.now())
                .price(BigDecimal.valueOf(1000L))
                .totalAmount(BigDecimal.valueOf(10000L))
                .build();

        TransactionHistory history2 = TransactionHistory.builder()
                .transactionType(TransactionType.ASK)
                .amount(BigDecimal.valueOf(10L))
                .settledAmount(BigDecimal.valueOf(10010L))
                .user(userSaved)
                .coin(coin)
                .commission(10L)
                .orderTime(LocalDateTime.now())
                .price(BigDecimal.valueOf(1000L))
                .totalAmount(BigDecimal.valueOf(10000L))
                .build();

        TransactionHistory history3 = TransactionHistory.builder()
                .transactionType(TransactionType.BID)
                .amount(BigDecimal.valueOf(10L))
                .settledAmount(BigDecimal.valueOf(10010L))
                .user(userSaved)
                .coin(coin)
                .commission(10L)
                .orderTime(LocalDateTime.now())
                .price(BigDecimal.valueOf(1000L))
                .totalAmount(BigDecimal.valueOf(10000L))
                .build();
        transactionHistoryRepository.save(history1);
        transactionHistoryRepository.save(history2);

        BigDecimal sum = transactionHistoryRepository.sumSettledAmountByTypeAndUserId(userSaved.getUserId(), TransactionType.ASK);

        assertThat(sum)
                .isEqualTo(BigDecimal.valueOf(2002000L, 2));
    }
}
