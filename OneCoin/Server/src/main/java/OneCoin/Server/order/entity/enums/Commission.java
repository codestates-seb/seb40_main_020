package OneCoin.Server.order.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public enum Commission {
    ORDER(new BigDecimal("1.05"));

    private final BigDecimal rate;
}
