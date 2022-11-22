package OneCoin.Server.upbit.dto.orderbook;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderBookDto {
    private String totalAskSize;
    private String totalBidSize;
    private List<askInfo> askInfo;
    private List<bidInfo> bidInfo;
}
