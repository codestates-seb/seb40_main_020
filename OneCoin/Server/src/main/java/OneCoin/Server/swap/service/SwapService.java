package OneCoin.Server.swap.service;

import OneCoin.Server.coin.service.CoinService;
import OneCoin.Server.swap.dto.SwapDto;
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
    private final BigDecimal swapCommission = new BigDecimal("0.0005");    // 수수료
    private final BigDecimal swapAmount = new BigDecimal("0.9995");        // 수수료 제외량

    public SwapService(SwapRepository swapRepository, TickerRepository tickerRepository, CoinService coinService) {
        this.swapRepository = swapRepository;
        this.tickerRepository = tickerRepository;
        this.coinService = coinService;
    }

    /**
     *  코인 환율 계산
     */
    public SwapDto.ExchangeRate calculateExchangeRate(String givenCoinCode, String takenCoinCode, BigDecimal amount) {
        // 코인 이름 체크
//        coinService.verifyCoinExists(givenCoinCode);
//        coinService.verifyCoinExists(takenCoinCode);

        SwapDto.ExchangeRate exchangeRate = new SwapDto.ExchangeRate();
        
        BigDecimal givenCoinPrice = new BigDecimal(tickerRepository.findTickerByCode(givenCoinCode).getTradePrice());
        BigDecimal takenCoinPrice = new BigDecimal(tickerRepository.findTickerByCode(takenCoinCode).getTradePrice());

        BigDecimal commissionAmount = amount.multiply(swapCommission);
        BigDecimal takeCoinAmount = givenCoinPrice.multiply(amount.multiply(swapAmount)).divide(takenCoinPrice, 15, RoundingMode.HALF_UP);

        exchangeRate.setCommission(commissionAmount.toString());
        exchangeRate.setTakenAmount(takeCoinAmount.toString());

        return exchangeRate;
    }
}
