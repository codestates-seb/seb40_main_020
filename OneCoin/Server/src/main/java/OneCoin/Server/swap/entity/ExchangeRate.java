package OneCoin.Server.swap.entity;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExchangeRate {
    private BigDecimal takenAmount;
    private BigDecimal commission;
    private BigDecimal givenCoinPrice;
    private BigDecimal takenCoinPrice;
}
