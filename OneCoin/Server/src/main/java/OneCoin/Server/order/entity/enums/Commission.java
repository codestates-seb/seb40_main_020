package OneCoin.Server.order.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
public enum Commission {
    ORDER(new BigDecimal("0.0005")),
    SWAP(new BigDecimal("0.0005"));

    @Getter
    private final BigDecimal rate;
}
