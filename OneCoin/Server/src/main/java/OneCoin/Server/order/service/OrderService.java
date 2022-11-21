package OneCoin.Server.order.service;

import OneCoin.Server.coin.entity.Coin;
import OneCoin.Server.coin.service.CoinService;
import OneCoin.Server.order.entity.Order;
import OneCoin.Server.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CoinService coinService;

    public Order createOrder(Order order, String code) {
        // TODO User mapping
        Coin coin = coinService.findVerifiedCoin(code);
        if (order.isAskBid()) { // 매도
        }
        if (!order.isAskBid()) { // 매수
            // 예외 확인
        }

        order.setCode(code);
        return orderRepository.save(order);
    }
}
