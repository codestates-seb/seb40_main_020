package OneCoin.Server.order.dto;

import OneCoin.Server.validator.MustHaveLimitOrMarket;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

public class RedisOrderDto {
    @Getter
    @Setter
    @MustHaveLimitOrMarket(limit = "limit", market = "market")
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
        @Range(min = 0, max = 1, message = "0 또는 1만 허용합니다.")
        private Integer askOrBid;
    }

    @Getter
    @Setter
    public static class PostResponse {
        private Long orderId;
    }

    @Getter
    @Setter
    public static class GetResponse {
        private String code;
        private LocalDateTime orderTime;
        private boolean askOrBid;
        private String limit;
        private String market; // 시장가에도 전부 체결되지 않을 가능성
        private String stopLimit;
        private String amount;
        private String completedAmount;
    }
}
