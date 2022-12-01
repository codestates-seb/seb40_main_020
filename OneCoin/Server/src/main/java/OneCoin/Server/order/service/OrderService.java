package OneCoin.Server.order.service;

import OneCoin.Server.balance.service.BalanceService;
import OneCoin.Server.coin.service.CoinService;
import OneCoin.Server.config.auth.utils.LoggedInUserInfoUtils;
import OneCoin.Server.exception.BusinessLogicException;
import OneCoin.Server.exception.ExceptionCode;
import OneCoin.Server.order.entity.Order;
import OneCoin.Server.order.entity.Wallet;
import OneCoin.Server.order.entity.enums.TransactionType;
import OneCoin.Server.order.repository.OrderRepository;
import OneCoin.Server.user.entity.User;
import OneCoin.Server.utils.CalculationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CoinService coinService;
    private final WalletService walletService;
    private final LoggedInUserInfoUtils loggedInUserInfoUtils;
    private final CalculationUtil calculationUtil;
    private final BalanceService balanceService;
    private final TransactionHistoryService transactionHistoryService;

    public void createOrder(Order order, String code) {
        User user = loggedInUserInfoUtils.extractUser();
        coinService.verifyCoinExists(code);
        long userId = user.getUserId();
        BigDecimal amount = order.getAmount();

        if (order.getOrderType().equals(TransactionType.ASK.getType())) { // 매도
            Wallet wallet = walletService.findVerifiedWalletWithCoin(userId, code);
            BigDecimal prevOrderAmount = getPrevAskOrderAmount(userId, code);
            checkUserCoinAmount(wallet, amount, prevOrderAmount);
        }
        if (order.getOrderType().equals(TransactionType.BID.getType())) { // 매수
            BigDecimal price = order.getLimit();
            subtractUserBalance(userId, price, amount);
        }
        order.setUserId(user.getUserId());
        order.setCode(code);
        orderRepository.save(order);
    }

    public void checkUserCoinAmount(Wallet wallet, BigDecimal orderAmount, BigDecimal prevOrderAmount) {
        BigDecimal myWalletAmount = wallet.getAmount();
        BigDecimal sellableAmount = myWalletAmount.subtract(prevOrderAmount);
        int comparison = sellableAmount.compareTo(orderAmount);

        if (comparison < 0) {
            throw new BusinessLogicException(ExceptionCode.NOT_ENOUGH_AMOUNT);
        }
    }

    public BigDecimal getPrevAskOrderAmount(long userId, String code) {
        List<Order> prevAskOrders = orderRepository.findAllByUserIdAndOrderTypeAndCode(userId, TransactionType.ASK.getType(), code);
        BigDecimal amount = BigDecimal.ZERO;
        if (prevAskOrders.isEmpty()) {
            return amount;
        }
        for (Order order : prevAskOrders) {
            amount = amount.add(order.getAmount());
        }
        return amount;
    }

    private void subtractUserBalance(long userId, BigDecimal price, BigDecimal amount) {
        BigDecimal totalBidPrice = calculationUtil.calculateByAddingCommission(price, amount);
        balanceService.updateBalanceByBid(userId, totalBidPrice);
    }

    public void cancelOrder(long orderId) {
        Order order = findVerifiedOrder(orderId);
        long userId = verifyUserOrder(order);

        if (order.getOrderType().equals(TransactionType.BID.getType())) { // 매수 주문 취소 시 balance 환불
            giveBalanceBack(userId, order.getLimit(), order.getAmount());
        }
        savePartialTradedOrdersToTransactionHistory(order);
        orderRepository.delete(order);
    }

    private Order findVerifiedOrder(long orderId) {
        Optional<Order> optionalRedisOrder = orderRepository.findById(orderId);
        return optionalRedisOrder.orElseThrow(() -> new BusinessLogicException(ExceptionCode.NO_EXISTS_ORDER));
    }

    private long verifyUserOrder(Order order) {
        User user = loggedInUserInfoUtils.extractUser();
        long userId = user.getUserId();
        if (order.getUserId() != userId) {
            throw new BusinessLogicException(ExceptionCode.NOT_YOUR_ORDER);
        }
        return userId;
    }

    private void giveBalanceBack(long userId, BigDecimal cancelPrice, BigDecimal cancelAmount) {
        BigDecimal totalCancelPrice = calculationUtil.calculateByAddingCommission(cancelPrice, cancelAmount);
        balanceService.updateBalanceByAskOrCancelBid(userId, totalCancelPrice);
    }

    private void savePartialTradedOrdersToTransactionHistory(Order order) {
        int comparison = order.getCompletedAmount().compareTo(BigDecimal.ZERO);
        if (comparison > 0) {
            transactionHistoryService.createTransactionHistory(order);
        }
    }

    @Transactional(readOnly = true)
    public List<Order> findOrders() {
        User user = loggedInUserInfoUtils.extractUser();
        List<Order> myOrders = orderRepository.findAllByUserId(user.getUserId());

        if (myOrders.isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.NO_EXISTS_ORDER);
        }
        return myOrders;
    }
}
