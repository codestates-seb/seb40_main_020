package OneCoin.Server.upbit.dto.orderbook;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AskInfo {
    private String askPrice;
    private String askSize;
    private String changeRate;
}
