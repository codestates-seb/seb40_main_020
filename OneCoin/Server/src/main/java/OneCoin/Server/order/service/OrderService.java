package OneCoin.Server.order.service;

import OneCoin.Server.coin.service.CoinService;
import OneCoin.Server.config.auth.utils.LoggedInUserInfoUtils;
import OneCoin.Server.exception.BusinessLogicException;
import OneCoin.Server.exception.ExceptionCode;
import OneCoin.Server.order.entity.Order;
import OneCoin.Server.order.entity.Wallet;
import OneCoin.Server.order.entity.enums.TransactionType;
import OneCoin.Server.order.repository.OrderRepository;
import OneCoin.Server.user.entity.User;
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

    public Order createOrder(Order order, String code) {
        User user = loggedInUserInfoUtils.extractUser();
        long userId = user.getUserId();
        coinService.findVerifiedCoin(code);

        if (order.getOrderType().equals(TransactionType.ASK.getType())) { // 매도
            Wallet wallet = verifyCoinInMyWallet(userId, code);
            checkUserCoinAmount(wallet, order.getAmount());
        }
        if (order.getOrderType().equals(TransactionType.BID.getType())) { // 매수
            BigDecimal price = getMyOrderPrice(order);
            updateUserBalance(user, price, order.getAmount());
        }
        order.setUserId(user.getUserId());
        order.setCode(code);
        return orderRepository.save(order);
    }

    private Wallet verifyCoinInMyWallet(long userId, String code) {
        Wallet wallet = walletService.findMyWallet(userId, code);
        if (wallet == null) {
            throw new BusinessLogicException(ExceptionCode.HAVE_NO_COIN);
        }
        return wallet;
    }

    private void checkUserCoinAmount(Wallet wallet, BigDecimal amount) {
        BigDecimal myAmount = wallet.getAmount();
        int comparison = myAmount.compareTo(amount);

        if (comparison < 0) {
            throw new BusinessLogicException(ExceptionCode.NOT_ENOUGH_AMOUNT);
        }
    }

    private void updateUserBalance(User user, BigDecimal price, BigDecimal amount) {
        BigDecimal balance = new BigDecimal(String.valueOf(100000000)); // TODO user.getBalance();
        BigDecimal totalBidPrice = price.multiply(amount);
        verifyEnoughBalance(balance, totalBidPrice);

        // TODO user balance 업데이트 - user쪽 서비스 로직으로 진행
    }

    private void verifyEnoughBalance(BigDecimal balance, BigDecimal totalBidPrice) {
        int comparison = balance.compareTo(totalBidPrice);
        if (comparison < 0) {
            throw new BusinessLogicException(ExceptionCode.NOT_ENOUGH_BALANCE);
        }
    }

    private BigDecimal getMyOrderPrice(Order order) {
        BigDecimal price = null;
        BigDecimal zero = BigDecimal.ZERO;
        if (order.getLimit().compareTo(zero) != 0) {
            price = order.getLimit();
        } else if (order.getMarket().compareTo(zero) != 0) {
            price = order.getMarket();
        } else if (order.getStopLimit().compareTo(zero) != 0) {
            price = order.getStopLimit();
        }
        return price;
    }

    public void cancelOrder(long orderId) {
        Order order = findVerifiedOrder(orderId);
        long userId = loggedInUserInfoUtils.extractUserId();
        verifySameUser(order, userId);
        // TODO balance 다시 돌려주기

        orderRepository.delete(order);
    }

    private Order findVerifiedOrder(long orderId) {
        Optional<Order> optionalRedisOrder = orderRepository.findById(orderId);
        return optionalRedisOrder.orElseThrow(() -> new BusinessLogicException(ExceptionCode.NO_EXISTS_ORDER));
    }

    private void verifySameUser(Order order, long userId) {
        if (order.getUserId() != userId) {
            throw new BusinessLogicException(ExceptionCode.NOT_YOUR_ORDER);
        }
    }

    @Transactional(readOnly = true)
    public List<Order> findOrders(String code) {
        long userId = loggedInUserInfoUtils.extractUserId();
        List<Order> myOrders = orderRepository.findAllByUserIdAndCode(userId, code);
        if (myOrders.isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.NO_EXISTS_ORDER);
        }
        return myOrders;
    }
}
