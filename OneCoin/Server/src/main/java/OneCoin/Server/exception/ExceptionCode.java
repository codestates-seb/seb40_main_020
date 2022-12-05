package OneCoin.Server.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExceptionCode {
    HAVE_NO_COIN(400, "You don't have this coin."),
    NO_EXISTS_WALLET(400, "No exists wallet."),
    NOT_ENOUGH_AMOUNT(400, "Not enough coin amount"),
    NOT_ENOUGH_BALANCE(400, "Not enough your balance"),
    NO_EXISTS_ORDER(400, "No exists order."),
    NOT_YOUR_ORDER(400, "It's not your order."),
    COIN_NOT_EXISTS(400, "Coin not exists."),
    NO_SUCH_NATION(404, "There is no such nation resistered"),
    NO_SUCH_CHAT_ROOM(404, "There is no such chat room"),
    CHAT_ROOM_NAME_EXISTS(409, "A chat room with the name already exists"),
    FAIL_TO_SERIALIZE(500, "직렬화에 실패했습니다."),
    USER_NOT_FOUND(404, "User not found"),
    USER_EXISTS(409, "User already exists"),
    INVALID_DESTINATION(400, "No match for the destination"),
    BALANCE_NOT_FOUND(404, "Balance not found"),
    AUTHENTICATION_NOT_FOUND(404, "No authentication found in SecurityContextHolder"),
    INVALID_CHAT_ROOM_ID(409, "ChatRoomId is not valid"),
    NOT_CORRECT_PERIOD(400, "It`s not correct period."),
    NOT_CORRECT_TYPE(400, "It`s not correct type."),
    NO_AUTHENTICATION_EMAIL(404, "Unauthenticated email"),
    AUTH_NOT_FOUND(404, "Auth data not found"),
    OCCURRED_NEGATIVE_AMOUNT(500, "Negative amount has occurred."),
    UNDEFINED_PLATFORM(404, "Platform not found"),
    NOT_VALID_AUTHENTICATION(404, "Unvalid authentication"),
    NO_CHAT_EXIST(404, "No chat in cache");

    private final int code;
    private final String description;
}
