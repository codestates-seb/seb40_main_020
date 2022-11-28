package OneCoin.Server.order.dto;

import lombok.Getter;
import lombok.Setter;

public class WalletDto {

    @Getter
    @Setter
    public static class GetResponse {
        private String code;
        private String amount;
        private String averagePrice;
    }
}
