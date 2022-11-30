package OneCoin.Server.upbit.dto.orderbook;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class OrderBookDto {
    private String code;
    private String totalAskSize;
    private String totalBidSize;
    private List<AskInfo> askInfo;
    private List<BidInfo> bidInfo;
}
