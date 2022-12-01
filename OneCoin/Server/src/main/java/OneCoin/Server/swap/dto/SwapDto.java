package OneCoin.Server.swap.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

public class SwapDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Post {
        @NotBlank
        private String givenCoin;

        @NotBlank
        private String takenCoin;

        @PositiveOrZero
        private String givenAmount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExchangeRate {
        private String takenAmount;
        private String commission;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long swapId;

        private String givenCoin;

        private String givenAmount;

        private String takenCoin;

        private String takenAmount;

        private String commission;

        private String givenCoinPrice;

        private String takenCoinPrice;
    }
}
