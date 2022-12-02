package OneCoin.Server.swap.service;

import OneCoin.Server.coin.service.CoinService;
import OneCoin.Server.order.entity.Wallet;
import OneCoin.Server.order.mapper.TransactionHistoryMapper;
import OneCoin.Server.order.service.OrderService;
import OneCoin.Server.order.service.TransactionHistoryService;
import OneCoin.Server.order.service.WalletService;
import OneCoin.Server.swap.entity.ExchangeRate;
import OneCoin.Server.swap.entity.Swap;
import OneCoin.Server.swap.mapper.SwapWalletMapper;
import OneCoin.Server.swap.repository.SwapRepository;
import OneCoin.Server.upbit.repository.TickerRepository;
import OneCoin.Server.user.entity.User;
import OneCoin.Server.user.service.UserService;
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
    private final UserService userService;
    private final OrderService orderService;
    private final WalletService walletService;
    private final SwapWalletMapper swapWalletMapper;
    private final TransactionHistoryService transactionHistoryService;
    private final BigDecimal swapCommission = new BigDecimal("0.0005");    // 수수료
    private final BigDecimal swapAmount = BigDecimal.ONE.subtract(swapCommission);        // 수수료 제외량

    public SwapService(SwapRepository swapRepository, TickerRepository tickerRepository, CoinService coinService, UserService userService, OrderService orderService, WalletService walletService, SwapWalletMapper swapWalletMapper, TransactionHistoryMapper transactionHistoryMapper, TransactionHistoryService transactionHistoryService) {
        this.swapRepository = swapRepository;
        this.tickerRepository = tickerRepository;
        this.coinService = coinService;
        this.userService = userService;
        this.orderService = orderService;
        this.walletService = walletService;
        this.swapWalletMapper = swapWalletMapper;
        this.transactionHistoryService = transactionHistoryService;
    }

    /**
     *  코인 환율 계산
     */
    public ExchangeRate calculateExchangeRate(String givenCoinCode, String takenCoinCode, BigDecimal amount) {
        // 코인 이름 체크
//        coinService.verifyCoinExists(givenCoinCode);
//        coinService.verifyCoinExists(takenCoinCode);

        ExchangeRate exchangeRate = new ExchangeRate();
        
        BigDecimal givenCoinPrice = new BigDecimal(tickerRepository.findTickerByCode(givenCoinCode).getTradePrice());
        BigDecimal takenCoinPrice = new BigDecimal(tickerRepository.findTickerByCode(takenCoinCode).getTradePrice());

        BigDecimal commissionAmount = amount.multiply(swapCommission);
        BigDecimal takeCoinAmount = givenCoinPrice.multiply(amount.multiply(swapAmount)).divide(takenCoinPrice, 15, RoundingMode.HALF_UP);

        exchangeRate.setCommission(commissionAmount);
        exchangeRate.setTakenAmount(takeCoinAmount);
        exchangeRate.setGivenCoinPrice(givenCoinPrice);
        exchangeRate.setTakenCoinPrice(takenCoinPrice);

        return exchangeRate;
    }

    /**
     *  코인 스왑
     */

    public Swap createSwap(Swap swap, Long userId, String givenCoinCode, String takenCoinCode, BigDecimal amount) {
        User user = userService.findUser(userId);
        ExchangeRate exchangeRate = calculateExchangeRate(givenCoinCode, takenCoinCode, amount);

        // 스왑 가능 코인 체크
        Wallet wallet = walletService.findVerifiedWalletWithCoin(userId, givenCoinCode);
        BigDecimal prevOrderAmount = orderService.getPrevAskOrderAmount(userId, givenCoinCode);
        orderService.checkUserCoinAmount(wallet, amount, prevOrderAmount);

        // 스왑 생성
        swap.setUser(user);
        swap.setGivenCoin(coinService.findCoin(givenCoinCode));
        swap.setTakenCoin(coinService.findCoin(takenCoinCode));
        swap.setTakenAmount(exchangeRate.getTakenAmount());
        swap.setCommission(exchangeRate.getCommission());
        swap.setGivenCoinPrice(exchangeRate.getGivenCoinPrice());
        swap.setTakenCoinPrice(exchangeRate.getTakenCoinPrice());

        // 코인 스왑(Wallet)

        
        // Transaction History 저장
        transactionHistoryService.createTransactionHistoryBySwap(swap);

        return swapRepository.save(swap);
    }
}
