package OneCoin.Server.order.service;

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

    public RedisOrder createOrder(RedisOrder redisOrder, String code) {
        // TODO User mapping
        coinService.findVerifiedCoin(code);
        if (redisOrder.isAskBid()) { // 매도
            // 예외 확인
        }
        if (!redisOrder.isAskBid()) { // 매수
            // 예외 확인
        }

        redisOrder.setCode(code);
        return redisOrderRepository.save(redisOrder);
    }
}
