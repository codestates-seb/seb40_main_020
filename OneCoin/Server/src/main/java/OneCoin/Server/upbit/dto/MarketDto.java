package OneCoin.Server.upbit.dto;

import OneCoin.Server.upbit.dto.orderbook.OrderBookDto;
import OneCoin.Server.upbit.dto.ticker.TickerDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MarketDto {
    private List<TickerDto> ticker;
    private List<OrderBookDto> orderBook;
}
