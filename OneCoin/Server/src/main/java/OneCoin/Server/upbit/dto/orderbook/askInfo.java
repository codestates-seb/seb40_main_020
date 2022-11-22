package OneCoin.Server.upbit.dto.orderbook;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class askInfo {
    private String askPrice;
    private String askSize;
    private String changeRate;
}
