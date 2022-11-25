package OneCoin.Server.upbit.dto.orderbook;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BidInfo {
    private String bidPrice;
    private String bidSize;
    private String changeRate;
}
