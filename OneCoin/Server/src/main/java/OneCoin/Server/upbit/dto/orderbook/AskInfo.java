package OneCoin.Server.upbit.dto.orderbook;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AskInfo {
    private String askPrice;
    private String askSize;
    private String changeRate;
}
