package OneCoin.Server.order.dto;

import OneCoin.Server.validator.MustHavePrice;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

public class OrderDto {
    @Getter
    @Setter
    @MustHavePrice(limit = "limit", market = "market")
    public static class Post {

        @PositiveOrZero(message = "음수 값은 허용하지 않습니다.")
        private double limit;

        @PositiveOrZero(message = "음수 값은 허용하지 않습니다.")
        private double market;

        @PositiveOrZero(message = "음수 값은 허용하지 않습니다.")
        private double stopLimit;

        @Positive(message = "필드가 비어있거나 0 또는 음수 값은 허용하지 않습니다.")
        private double amount;

        @NotNull(message = "빈 필드는 허용하지 않습니다.")
        @Pattern(regexp = "^ASK$|^BID$", message = "매도는 ASK, 매수는 BID를 입력해야 합니다.")
        private String orderType;
    }

    @Getter
    @Setter
    public static class GetResponse {
        private long orderId;
        private String code;
        private String orderTime;
        private String orderType;
        private String limit;
        private String market;
        private String stopLimit;
        private String amount;
        private String completedAmount;
    }
}
