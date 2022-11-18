package OneCoin.Server.exception;

import lombok.Getter;

@Getter
public class BusinessLogicException extends RuntimeException{

    private ExceptionCode exceptionCode;

    public BusinessLogicException(ExceptionCode exceptionCode) {
        this.exceptionCode = exceptionCode;
    }
}
