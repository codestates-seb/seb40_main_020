package OneCoin.Server.order.service;

import OneCoin.Server.order.entity.RedisOrder;
import OneCoin.Server.order.repository.RedisOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final RedisOrderRepository redisOrderRepository;

    public RedisOrder createAskOrder(RedisOrder redisOrder) {
        // TODO User mapping
        redisOrder.setAskOrBid(true);
        return redisOrderRepository.save(redisOrder);
    }

    public RedisOrder createBidOrder(RedisOrder redisOrder) {
        // TODO User mapping

        return redisOrderRepository.save(redisOrder);
    }
}
