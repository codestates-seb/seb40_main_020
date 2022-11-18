package OneCoin.Server.chat.util;

import OneCoin.Server.exception.BusinessLogicException;
import OneCoin.Server.exception.ExceptionCode;
import lombok.Getter;

import java.util.Arrays;

public enum Nation {
    KR("kr"), JP("jp"), US("us"), EU("eu");
    @Getter
    private String name;

    Nation(String name) {
        this.name = name;
    }

    public static void verifyNation(String nation) {
        Arrays.stream(Nation.values())
                .filter(registeredNation -> registeredNation.getName().equals(nation))
                .findAny()
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.NO_SUCH_NATION));
    }
}
