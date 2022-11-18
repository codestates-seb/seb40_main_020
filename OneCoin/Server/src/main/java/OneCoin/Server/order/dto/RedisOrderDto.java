package OneCoin.Server.order.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class RedisOrderDto {
    // TODO valid
    @Getter
    @Setter
    public static class Post { // TODO limit market 둘다 null일 때 유효성 검증 구현
        private String limit;
        private String market;
        private String stopLimit;
        @NotNull
        private String amount;
    }

    @Getter
    @Setter
    public static class PostResponse {
        private Long orderId;
    }

    @Getter
    @Setter
    public static class GetResponse {
        private LocalDateTime orderTime;
        private String code;
        private boolean askOrBid;
        private String limit;
        private String stopLimit;
        private String amount;
        private String completedAmount;
    }
}
