package OneCoin.Server.order.dto;

import lombok.Getter;
import lombok.Setter;

public class TransactionHistoryDto {

    @Getter
    @Setter
    public static class GetResponse {
        private String code;
        private String completedTime;
        private String orderType;
        private String amount;
        private String price;
        private String totalAmount;
        private String commission;
        private String settledAmount;
        private String orderTime;
    }
}
