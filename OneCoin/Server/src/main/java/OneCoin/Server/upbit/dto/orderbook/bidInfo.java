package OneCoin.Server.upbit.dto.orderbook;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class bidInfo {
    private double bidPrice;
    private double bidSize;
    private double changeRate;
}
