package OneCoin.Server.order.service;

import OneCoin.Server.coin.entity.Coin;
import OneCoin.Server.coin.service.CoinService;
import OneCoin.Server.order.entity.RedisOrder;
import OneCoin.Server.order.repository.RedisOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final RedisOrderRepository redisOrderRepository;
    private final CoinService coinService;

    // TODO User mapping
    public RedisOrder createOrder(RedisOrder redisOrder, String code) {
        Coin orderedCoin = coinService.findCoin(code);
        if (redisOrder.isAskOrBid()) {
            // 예외 확인
        }
        if (!redisOrder.isAskOrBid()) {
            // 예외 확인
        }

        redisOrder.setCoin(orderedCoin);
        return redisOrderRepository.save(redisOrder);
    }
}
