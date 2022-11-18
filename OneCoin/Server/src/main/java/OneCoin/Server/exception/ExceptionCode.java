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
    NO_SUCH_NATION(404, "There is no such nation resistered"),
    NO_SUCH_CHAT_ROOM(404, "There is no such chat room"),
    CHAT_ROOM_NAME_EXISTS(409, "A chat room with the name already exists"),
    FAIL_TO_SERIALIZE(500, "직렬화에 실패했습니다.");

    private final int code;
    private final String description;
}
