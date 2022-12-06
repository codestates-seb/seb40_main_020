package OneCoin.Server.swap.service;

import OneCoin.Server.coin.service.CoinService;
import OneCoin.Server.order.entity.Wallet;
import OneCoin.Server.order.entity.enums.Commission;
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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Transactional
@RequiredArgsConstructor
public class SwapService {
    private final SwapRepository swapRepository;
    private final TickerRepository tickerRepository;
    private final CoinService coinService;
    private final UserService userService;
    private final OrderService orderService;
    private final WalletService walletService;
    private final SwapWalletMapper swapWalletMapper;
    private final TransactionHistoryService transactionHistoryService;
    private final BigDecimal swapCommission = Commission.SWAP.getRate();    // 수수료
    private final BigDecimal swapAmount = BigDecimal.ONE.subtract(swapCommission);        // 수수료 제외량

    /**
     * <pre>
     *     코인 환율 계산
     *  </pre>
     */
    public ExchangeRate calculateExchangeRate(String givenCoinCode, String takenCoinCode, BigDecimal amount) {
        // 코인 이름 체크
        coinService.verifyCoinExists(givenCoinCode);
        coinService.verifyCoinExists(takenCoinCode);

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
     * <pre>
     *      코인 스왑
     * </pre>
     */

    public Swap createSwap(Swap swap, Long userId) {
        User user = userService.findUser(userId);
        String givenCoinCode = swap.getGivenCoin().getCode();
        String takenCoinCode = swap.getTakenCoin().getCode();
        BigDecimal amount = swap.getGivenAmount();

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
        // given
        Wallet givenWallet = swapWalletMapper.swapToGivenWallet(swap);
        walletService.updateWalletByGivenSwap(wallet, givenWallet);

        // taken
        Wallet takenWallet = swapWalletMapper.swapToTakenWallet(swap);
        Wallet findWallet = walletService.findMyWallet(userId, takenCoinCode);

        // wallet 이 없다면 새로 생성
        if (findWallet != null) {
            walletService.updateWalletByTakenSwap(findWallet, takenWallet);
        } else {
            walletService.createWalletByTakenSwap(takenWallet);
        }
        
        // Transaction History 저장
        transactionHistoryService.createTransactionHistoryBySwap(swap);

        return swapRepository.save(swap);
    }

    /**
     * <pre>
     *     스왑 정보 리스트 가져오기
     * </pre>
     */
    public Page<Swap> findSwaps(int page, int size) {
        return swapRepository.findAll(PageRequest.of(page, size));
    }
}
