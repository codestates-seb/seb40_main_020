package OneCoin.Server.upbit.entity.enums;

import lombok.Getter;

@Getter
public enum SiseType {
    TICKER("ticker"),
    TRADE("trade"),
    ORDER_BOOK("orderbook");

    private final String type;

    SiseType(String type) {
        this.type = type;
    }
}
