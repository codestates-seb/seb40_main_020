package OneCoin.Server.upbit.dto.orderbook;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BidInfo {
    private String bidPrice;
    private String bidSize;
    private String changeRate;
}
