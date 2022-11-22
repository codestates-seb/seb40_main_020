package OneCoin.Server.upbit.entity;

public enum SiseType {
    TICKER("ticker"),
    TRADE("trade"),
    ORDER_BOOK("orderbook");

    private final String type;

    SiseType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
