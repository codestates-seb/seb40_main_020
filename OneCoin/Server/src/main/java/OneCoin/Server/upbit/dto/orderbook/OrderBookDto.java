package OneCoin.Server.upbit.dto.orderbook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class OrderBookDto {

    private String code;

    @JsonProperty("total_ask_size")
    private String totalAskSize;

    @JsonProperty("total_bid_size")
    private String totalBidSize;

    @Setter
    private List<AskInfo> askInfo;

    @Setter
    private List<BidInfo> bidInfo;
}
