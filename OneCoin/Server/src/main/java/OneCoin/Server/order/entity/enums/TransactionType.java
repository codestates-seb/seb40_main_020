package OneCoin.Server.order.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum TransactionType {
    ASK("ASK"),
    BID("BID"),
    DEPOSIT("DEPOSIT"),
    SWAP("SWAP");

    @Getter
    private final String type;
}
