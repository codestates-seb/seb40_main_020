package OneCoin.Server.order.service;

import OneCoin.Server.balance.service.BalanceService;
import OneCoin.Server.config.auth.utils.LoggedInUserInfoUtils;
import OneCoin.Server.exception.BusinessLogicException;
import OneCoin.Server.exception.ExceptionCode;
import OneCoin.Server.order.entity.Order;
import OneCoin.Server.order.entity.Wallet;
import OneCoin.Server.order.mapper.WalletOrderMapper;
import OneCoin.Server.order.repository.OrderRepository;
import OneCoin.Server.order.repository.WalletRepository;
import OneCoin.Server.user.entity.User;
import OneCoin.Server.utils.CalculationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final OrderRepository orderRepository;
    private final WalletOrderMapper mapper;
    private final BalanceService balanceService;
    private final TransactionHistoryService transactionHistoryService;
    private final CalculationUtil calculationUtil;
    private final LoggedInUserInfoUtils loggedInUserInfoUtils;

    public void createWallet(Order order, BigDecimal tradeVolume) {
        BigDecimal completedAmount = getCompletedAmount(order, tradeVolume);
        Wallet newWallet = mapper.bidOrderToNewWallet(order, completedAmount);
        walletRepository.save(newWallet);
    }

    public void updateWalletByBid(Wallet wallet, Order order, BigDecimal tradeVolume) {
        BigDecimal completedAmount = getCompletedAmount(order, tradeVolume);
        Wallet updatedWallet = mapper.bidOrderToUpdatedWallet(wallet, order.getLimit(), completedAmount);
        walletRepository.save(updatedWallet);
    }

    public void updateWalletByAsk(Wallet wallet, Order order, BigDecimal tradeVolume) {
        BigDecimal completedAmount = getCompletedAmount(order, tradeVolume);
        Wallet updatedWallet = mapper.askOrderToUpdatedWallet(wallet, completedAmount);
        if (verifyWalletAmountZero(updatedWallet)) {
            walletRepository.delete(updatedWallet);
        } else {
            walletRepository.save(updatedWallet);
        }
        addUserBalance(order.getUserId(), order.getLimit(), completedAmount);
    }

    private BigDecimal getCompletedAmount(Order order, BigDecimal tradeVolume) {
        BigDecimal orderAmount = order.getAmount();

        int comparison = orderAmount.compareTo(tradeVolume);
        if (comparison <= 0) { // 전부 체결된 주문
            order.setCompletedAmount(order.getCompletedAmount().add(orderAmount));
            deleteCompletedOrder(order);
            return orderAmount;
        }
        saveRemainingAmount(order, tradeVolume);
        return tradeVolume;
    }

    private void saveRemainingAmount(Order order, BigDecimal completedAmount) {
        order.setAmount(order.getAmount().subtract(completedAmount));
        order.setCompletedAmount(order.getCompletedAmount().add(completedAmount));
        orderRepository.save(order);
    }

    private void deleteCompletedOrder(Order order) {
        transactionHistoryService.createTransactionHistoryByOrder(order);
        orderRepository.delete(order);
    }

    private boolean verifyWalletAmountZero(Wallet wallet) {
        BigDecimal amount = wallet.getAmount();
        int comparison = amount.compareTo(BigDecimal.ZERO);
        if (comparison < 0) {
            throw new BusinessLogicException(ExceptionCode.OCCURRED_NEGATIVE_AMOUNT);
        }
        return comparison == 0;
    }

    private void addUserBalance(long userId, BigDecimal price, BigDecimal completedAmount) {
        BigDecimal totalAskPrice = calculationUtil.calculateBySubtractingCommission(price, completedAmount);
        balanceService.updateBalanceByAskOrCancelBid(userId, totalAskPrice);
    }

    public Wallet findVerifiedWalletWithCoin(long userId, String code) {
        Wallet wallet = findMyWallet(userId, code);
        if (wallet == null) {
            throw new BusinessLogicException(ExceptionCode.HAVE_NO_COIN);
        }
        return wallet;
    }

    public Wallet findMyWallet(long userId, String code) {
        return walletRepository.findByUserIdAndCode(userId, code).orElse(null);
    }

    public List<Wallet> findWallets() {
        User user = loggedInUserInfoUtils.extractUser();
        List<Wallet> wallets = walletRepository.findAllByUserId(user.getUserId());
        if (wallets.isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.NO_EXISTS_WALLET);
        }
        return wallets;
    }

    public void createWalletByTakenSwap(Wallet takenWallet) {
        walletRepository.save(takenWallet);
    }

    public void updateWalletByGivenSwap(Wallet wallet, Wallet givenWallet) {
        BigDecimal amount = wallet.getAmount().subtract(givenWallet.getAmount());
        wallet.setAmount(amount);

        if (verifyWalletAmountZero(wallet)) {
            walletRepository.delete(wallet);
        } else {
            walletRepository.save(wallet);
        }
    }

    public void updateWalletByTakenSwap(Wallet wallet, Wallet takenWallet) {
        BigDecimal amount = wallet.getAmount().add(takenWallet.getAmount());
        BigDecimal averagePrice = calculationUtil.calculateAvgPrice(wallet.getAveragePrice(), wallet.getAmount(), takenWallet.getAveragePrice(), takenWallet.getAmount());

        wallet.setAmount(amount);
        wallet.setAveragePrice(averagePrice);

        walletRepository.save(wallet);
    }
}
