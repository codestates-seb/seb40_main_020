package OneCoin.Server.upbit.service;

import OneCoin.Server.upbit.dto.orderbook.OrderBookDto;
import OneCoin.Server.upbit.dto.ticker.TickerDto;
import OneCoin.Server.upbit.entity.Trade;
import OneCoin.Server.upbit.entity.UnitInfo;
import OneCoin.Server.upbit.entity.enums.SiseType;
import OneCoin.Server.upbit.mapper.OrderBookDtoMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UpbitHandlingService {
    private final ObjectMapper objectMapper;
    private final TradingService tradingService;
    private final OrderBookDtoMapper mapper;
    private final RedisTemplate<String, Object> redisTemplate;

    private String prevClosingPrice;

    @SneakyThrows
    public void parsing(JsonNode jsonNode) {
        String orderType = jsonNode.get("type").asText();

        if (orderType.equals(SiseType.TICKER.getType())) {
            TickerDto tickerDto = objectMapper.readValue(jsonNode.toString(), TickerDto.class);
            saveTicker(tickerDto);
            prevClosingPrice = tickerDto.getPrevClosingPrice();
            Trade trade = objectMapper.readValue(jsonNode.toString(), Trade.class);
//            tradingService.completeOrders(trade);
        }
        if (orderType.equals(SiseType.ORDER_BOOK.getType())) {
            OrderBookDto orderBookDto = objectMapper.readValue(jsonNode.toString(), OrderBookDto.class);
            List<UnitInfo> unitInfos = Arrays.asList(objectMapper.readValue(jsonNode.get("orderbook_units").toString(), UnitInfo[].class));
            orderBookDto = mapper.unitInfoToOrderBookDto(orderBookDto, unitInfos, prevClosingPrice);
            saveOrderBook(orderBookDto);
        }
    }

    private void saveTicker(TickerDto tickerDto) {
        HashOperations<String, String, TickerDto> operations = redisTemplate.opsForHash();
        operations.put(SiseType.TICKER.getType(), tickerDto.getCode(), tickerDto);
    }

    private void saveOrderBook(OrderBookDto orderBookDto) {
        HashOperations<String, String, OrderBookDto> operations = redisTemplate.opsForHash();
        operations.put(SiseType.ORDER_BOOK.getType(), orderBookDto.getCode(), orderBookDto);
    }
}
