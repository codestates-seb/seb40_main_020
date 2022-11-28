package OneCoin.Server.deposit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class DepositDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Post {
        @Positive(message = "0원을 초과한 금액을 입금하여야 합니다.")
        private Long depositAmount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long depositAmount;
        private BigDecimal remainingBalance;
        private String createdAt;
    }
}
