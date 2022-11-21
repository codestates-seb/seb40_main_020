package OneCoin.Server.upbit.dto.orderbook;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class askInfo {
    private double askPrice;
    private double askSize;
    private double changeRate;
}
