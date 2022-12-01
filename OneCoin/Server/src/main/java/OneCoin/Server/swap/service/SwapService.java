package OneCoin.Server.swap.service;

import OneCoin.Server.coin.service.CoinService;
import OneCoin.Server.swap.repository.SwapRepository;
import OneCoin.Server.upbit.repository.TickerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Transactional
@Service
public class SwapService {
    private final SwapRepository swapRepository;
    private final TickerRepository tickerRepository;
    private final CoinService coinService;
    private final BigDecimal swapCommission = new BigDecimal("0.00025");

    public SwapService(SwapRepository swapRepository, TickerRepository tickerRepository, CoinService coinService) {
        this.swapRepository = swapRepository;
        this.tickerRepository = tickerRepository;
        this.coinService = coinService;
    }

    /**
     *  코인 환율 계산
     */
    private BigDecimal calculateExchangeRate(String givenCoinCode, String takenCoinCode, BigDecimal amount) {
        // 코인 이름 체크
        coinService.verifyCoinExists(givenCoinCode);
        coinService.verifyCoinExists(takenCoinCode);
        
        BigDecimal givenCoinPrice = new BigDecimal(tickerRepository.findTickerByCode(givenCoinCode).getTradePrice());
        BigDecimal takenCoinPrice = new BigDecimal(tickerRepository.findTickerByCode(takenCoinCode).getTradePrice());

        return givenCoinPrice.divide(takenCoinPrice, 15, RoundingMode.HALF_UP).multiply(amount);
    }
}
