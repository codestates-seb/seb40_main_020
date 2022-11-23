package OneCoin.Server.upbit.dto;

import OneCoin.Server.upbit.dto.orderbook.OrderBookDto;
import OneCoin.Server.upbit.dto.ticker.TickerDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarketDto {
    private TickerDto ticker;
    private OrderBookDto orderBook;
}
