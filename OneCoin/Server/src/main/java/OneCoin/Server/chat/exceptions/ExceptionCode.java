package OneCoin.Server.chat.exceptions;

import lombok.Getter;

public enum ExceptionCode {
    NO_SUCH_NATION(404, "There is no such nation resistered"),
    NO_SUCH_CHAT_ROOM(404, "There is no such chat room"),
    CHAT_ROOM_NAME_EXISTS(409, "A chat room with the name already exists");
    @Getter
    private int code;
    @Getter
    private String message;

    ExceptionCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
