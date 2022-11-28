package OneCoin.Server.upbit.repository;

import OneCoin.Server.upbit.dto.orderbook.OrderBookDto;
import OneCoin.Server.upbit.entity.enums.CoinList;
import OneCoin.Server.upbit.entity.enums.SiseType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderBookRepository {
    private final RedisTemplate<String, Object> redisTemplate;

    public void saveOrderBook(OrderBookDto orderBookDto) {
        HashOperations<String, String, OrderBookDto> operations = redisTemplate.opsForHash();
        operations.put(SiseType.ORDER_BOOK.getType(), orderBookDto.getCode(), orderBookDto);
    }

    public List<OrderBookDto> findOrderBooks() {
        HashOperations<String, String, OrderBookDto> operations = redisTemplate.opsForHash();
        return operations.multiGet(SiseType.ORDER_BOOK.getType(), CoinList.CODES);
    }
}
