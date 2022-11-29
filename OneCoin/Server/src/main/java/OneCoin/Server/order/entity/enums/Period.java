package OneCoin.Server.order.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Period {
    WEEK("w"),
    MONTH("m"),
    THREE_MONTHS("3m"),
    SIX_MONTHS("6m");

    @Getter
    private final String abbreviation;
}
