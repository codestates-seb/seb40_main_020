package OneCoin.Server.rank.service;

import OneCoin.Server.order.entity.Wallet;
import OneCoin.Server.order.repository.WalletRepository;
import OneCoin.Server.rank.dao.UserRoi;
import OneCoin.Server.upbit.dto.ticker.TickerDto;
import OneCoin.Server.upbit.repository.TickerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class RankServiceTest {
    @Autowired
    private RankService rankService;
    @Autowired
    private  WalletRepository walletRepository;
    @Autowired
    private TickerRepository tickerRepository;

    @Test
    void init() {
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
                .amount(BigDecimal.valueOf(0.001))
                .averagePrice(BigDecimal.valueOf(10L))
                .userId(2L)
                .code("KRW-BTC")
                .build();
        walletRepository.save(wallet1);
        walletRepository.save(wallet2);
        walletRepository.save(wallet3);
    }

    @Test
    void calculateRoiTest() {
        List<TickerDto> ts = tickerRepository.findTickers();
        List<UserRoi> userROIList = rankService.calculateAllRois();
        //then
        assertThat(userROIList.size()).isEqualTo(2);
        assertThat(userROIList.get(0).getTotalRoi())
                .isGreaterThan(-2.0)
                .isLessThan(2.0);
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
