package OneCoin.Server.upbit.repository;

import OneCoin.Server.upbit.dto.ticker.TickerDto;
import OneCoin.Server.upbit.entity.enums.CoinList;
import OneCoin.Server.upbit.entity.enums.SiseType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TickerRepository {
    private final RedisTemplate<String, Object> redisTemplate;

    public void saveTicker(TickerDto tickerDto) {
        HashOperations<String, String, TickerDto> operations = redisTemplate.opsForHash();
        operations.put(SiseType.TICKER.getType(), tickerDto.getCode(), tickerDto);
    }

    public List<TickerDto> findTickers() {
        HashOperations<String, String, TickerDto> operations = redisTemplate.opsForHash();
        return operations.multiGet(SiseType.TICKER.getType(), CoinList.CODES);
    }
}
