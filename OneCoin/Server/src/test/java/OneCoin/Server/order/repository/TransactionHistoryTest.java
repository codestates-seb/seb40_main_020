package OneCoin.Server.order.repository;

import OneCoin.Server.coin.repository.CoinRepository;
import OneCoin.Server.rank.dao.UserRoi;
import OneCoin.Server.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
    void findAskTest() {
        List<UserRoi> userROI = transactionHistoryRepository.findAllSumOfAskSettledAmount();
        assertThat(userROI.size())
                .isEqualTo(1);
        assertThat(userROI.get(0).getSumOfAsks())
                .isEqualTo(450000.0);
    }

    @Test
    void findBidTest() {
        List<UserRoi> userROI = transactionHistoryRepository.findAllSumOfBidSettledAmount();
        assertThat(userROI.size())
                .isEqualTo(2);
        assertThat(userROI.get(0).getSumOfBids())
                .isEqualTo(260000.0);
    }
}
