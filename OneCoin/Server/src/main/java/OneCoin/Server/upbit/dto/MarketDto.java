package OneCoin.Server.upbit.dto;

import OneCoin.Server.upbit.dto.orderbook.OrderBookDto;
import OneCoin.Server.upbit.dto.ticker.TickerDto;

public class MarketDto {
    private String code;
    private String coinName;
    private TickerDto ticker;
    private OrderBookDto orderBook;
}
