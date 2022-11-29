package OneCoin.Server.upbit.repository;

import OneCoin.Server.upbit.dto.ticker.TickerDto;
import OneCoin.Server.upbit.entity.enums.CoinList;
import OneCoin.Server.upbit.entity.enums.SiseType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TickerRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private HashOperations<String, String, TickerDto> operations;

    @PostConstruct
    private void init() {
        operations = redisTemplate.opsForHash();
    }

    public void saveTicker(TickerDto tickerDto) {
        operations.put(SiseType.TICKER.getType(), tickerDto.getCode(), tickerDto);
    }

    public List<TickerDto> findTickers() {
        return operations.multiGet(SiseType.TICKER.getType(), CoinList.CODES);
    }

    public TickerDto findTickerByCode(String code) {
        return objectMapper.convertValue(operations.get(SiseType.TICKER.getType(), code), TickerDto.class);
    }

    public String findPrevClosingPrice(String code) {
        TickerDto tickerDto = objectMapper.convertValue(operations.get(SiseType.TICKER.getType(), code), TickerDto.class);
        return tickerDto.getPrevClosingPrice();
    }
}
