package OneCoin.Server.order.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

public class RedisOrderDto {
    @Getter
    @Setter
    public static class Post { // TODO limit market 둘다 null일 때 유효성 검증 구현
        @NotBlank
        private String code;

        @PositiveOrZero
        private double limit;

        @PositiveOrZero
        private double market;

        @PositiveOrZero
        private double stopLimit;

        @NotEmpty
        @PositiveOrZero
        private double amount;

        @NotEmpty
        private boolean askOrBid;
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
