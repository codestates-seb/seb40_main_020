package OneCoin.Server.order.service;

import OneCoin.Server.coin.entity.Coin;
import OneCoin.Server.coin.service.CoinService;
import OneCoin.Server.config.auth.utils.LoggedInUserInfoUtils;
import OneCoin.Server.exception.BusinessLogicException;
import OneCoin.Server.exception.ExceptionCode;
import OneCoin.Server.order.entity.Order;
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
    private final LoggedInUserInfoUtils loggedInUserInfoUtils;

    public Order createOrder(Order order, String code) {
        // TODO User mapping
        User user = loggedInUserInfoUtils.extractUser();
        Coin coin = coinService.findVerifiedCoin(code);
        if (order.getOrderType().equals("ASK")) { // 매도 // TODO ask, bid enum으로
            checkUserHasCoin(coin);
            checkUserCoinAmount(1L, order.getAmount());
        }
        if (order.getOrderType().equals("BID")) { // 매수
            // 예외 확인
        }
        order.setUserId(user.getUserId());
        order.setCode(code);
        return orderRepository.save(order);
    }

    // TODO 코인, 가격, 똑같은 주문이 있으면 덮어쓰기
    private long checkUserHasCoin(Coin coin) { // user가 해당 코인을 가지고 있는지 확인 - User로 리턴
        // TODO jwt User확인, wallet에 코인 있는지 확인
        long userId = 1L;
        // walletService ..
        // if(Wallet.)
        if (1 == 0) {
            throw new BusinessLogicException(ExceptionCode.HAVE_NO_COIN);
        }
        return userId;
    }

    private void checkUserCoinAmount(long userId, BigDecimal amount) { // user가 보유한 코인의 양보다 작거나 같은지 확인 - user의 지갑
        // TODO parameter User로 변경
        // BigDecimal myAmount = use
        BigDecimal myAmount = BigDecimal.valueOf(1000);
        int comparison = myAmount.compareTo(amount);

        if (comparison < 0) {
            throw new BusinessLogicException(ExceptionCode.NOT_ENOUGH_AMOUNT);
        }
    }

    // 매수 시
    // user의 보유 금액으로 매수할 수 있는 지 확인 - user의 balance
    private void checkUserBalance(BigDecimal price, BigDecimal amount) {
        // TODO jwt User의 것만 찾기
        long userId = 1L;
        // User user = userService.find()..
        // BigDecimal balance = user.getBalance();
        BigDecimal balance = BigDecimal.valueOf(10000);

        int comparison = balance.compareTo(price.multiply(amount));
        if (comparison < 0) {
            throw new BusinessLogicException(ExceptionCode.NOT_ENOUGH_BALANCE);
        }
    }

    public void cancelOrder(long orderId) {
        Order order = verifiedOrder(orderId);
        isSameUser(order);

        orderRepository.delete(order);
    }

    private Order verifiedOrder(long orderId) {
        Optional<Order> optionalRedisOrder = orderRepository.findById(orderId);
        return optionalRedisOrder.orElseThrow(() -> new BusinessLogicException(ExceptionCode.NO_EXISTS_ORDER));
    }

    private void isSameUser(Order order) {
        // TODO redisOrder에 매핑된 UserId와 취소를 요청하는 UserId가 같은지 확인
        long userId = 1L;
        if (order.getUserId() != userId) {
            throw new BusinessLogicException(ExceptionCode.NOT_YOUR_ORDER);
        }
    }

    @Transactional(readOnly = true)
    public List<Order> findOrders(String code) {
        // TODO jwt User의 것만 찾기
        long userId = 1L;
        return orderRepository.findAllByUserIdAndCode(userId, code);
    }

    public List<Order> findOrdersMatchingPrice(String price, String orderType, String code) {
        return orderRepository.findAllByLimitAndOrderTypeAndCode(new BigDecimal(price), orderType, code);
    }

    public void updateAmountAfterTrade(List<Order> orders) {
        for (Order order : orders) {
            deleteAmountZeroEntity(order);
        }
        orderRepository.saveAll(orders);
    }

    private void deleteAmountZeroEntity(Order order) {
        BigDecimal zero = BigDecimal.ZERO;
        if (order.getAmount().compareTo(zero) == 0) {
            orderRepository.delete(order);
        }
    }
}
