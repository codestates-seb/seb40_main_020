package OneCoin.Server.upbit.dto.orderbook;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderBookDto {
    private double totalAskSize;
    private double totalBidSize;
    private List<askInfo> askInfo;
    private List<bidInfo> bidInfo;
}
