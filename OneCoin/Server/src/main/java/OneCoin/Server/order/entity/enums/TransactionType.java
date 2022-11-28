package OneCoin.Server.order.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TransactionType {
    ALL("ALL"),
    ASK("ASK"),
    BID("BID"),
    DEPOSIT("DEPOSIT"),
    SWAP("SWAP");

    private final String type;
}
