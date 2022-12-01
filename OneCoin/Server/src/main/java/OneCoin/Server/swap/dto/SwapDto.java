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
        private Long givenAmount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long swapId;

        private String givenCoin;

        private Long givenAmount;

        private String takenCoin;

        private Long takenAmount;

        private Long commission;

        private Long givenCoinPrice;

        private Long takenCoinPrice;
    }
}
