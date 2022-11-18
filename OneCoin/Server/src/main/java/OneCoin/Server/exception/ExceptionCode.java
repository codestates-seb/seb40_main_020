package OneCoin.Server.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCode {
    HAVE_NO_COIN(400, "You don't have this coin."),
    NOT_ENOUGH_AMOUNT(400, "Not enough coin amount"),
    NOT_ENOUGH_BALANCE(400, "Not enough your balance"),
    NO_EXISTS_ORDER(400, "No exists order."),
    NOT_YOUR_ORDER(400, "It's not your order."),
    USER_NOT_FOUND(404, "User not found"),
    USER_EXISTS(409, "User already exists");


    private final int code;
    private final String description;
}
