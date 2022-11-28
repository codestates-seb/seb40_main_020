package OneCoin.Server.upbit.service;

import OneCoin.Server.upbit.dto.orderbook.OrderBookDto;
import OneCoin.Server.upbit.dto.ticker.TickerDto;
import OneCoin.Server.upbit.entity.Trade;
import OneCoin.Server.upbit.entity.UnitInfo;
import OneCoin.Server.upbit.entity.enums.SiseType;
import OneCoin.Server.upbit.mapper.OrderBookDtoMapper;
import OneCoin.Server.upbit.repository.OrderBookRepository;
import OneCoin.Server.upbit.repository.TickerRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UpbitHandlingService {
    private final ObjectMapper objectMapper;
    private final TradingService tradingService;
    private final OrderBookDtoMapper mapper;
    private final TickerRepository tickerRepository;
    private final OrderBookRepository orderBookRepository;

    private String prevClosingPrice;

    public void parsing(JsonNode jsonNode) {
        String type = jsonNode.get("type").asText();

        if (type.equals(SiseType.TICKER.getType())) { // 현재가 정보
            handleTicker(jsonNode);
        }
        if (type.equals(SiseType.ORDER_BOOK.getType())) { // 호가 정보
            handleOrderBook(jsonNode);
        }
    }

    @SneakyThrows
    private void handleTicker(JsonNode jsonNode) {
        TickerDto tickerDto = objectMapper.readValue(jsonNode.toString(), TickerDto.class);
        prevClosingPrice = tickerDto.getPrevClosingPrice();
        tickerRepository.saveTicker(tickerDto);

        Trade trade = objectMapper.readValue(jsonNode.toString(), Trade.class);
        tradingService.completeOrders(trade);
    }

    @SneakyThrows
    private void handleOrderBook(JsonNode jsonNode) {
        OrderBookDto orderBookDto = objectMapper.readValue(jsonNode.toString(), OrderBookDto.class);
        List<UnitInfo> unitInfos = Arrays.asList(objectMapper.readValue(jsonNode.get("orderbook_units").toString(), UnitInfo[].class));
        orderBookDto = mapper.unitInfoToOrderBookDto(orderBookDto, unitInfos, prevClosingPrice);
        orderBookRepository.saveOrderBook(orderBookDto);
    }
}
