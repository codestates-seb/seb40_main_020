package OneCoin.Server.upbit.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UnitInfo {
    @JsonProperty("ask_price")
    private String askPrice;

    @JsonProperty("bid_price")
    private String bidPrice;

    @JsonProperty("ask_size")
    private String askSize;

    @JsonProperty("bid_size")
    private String bidSize;
}
