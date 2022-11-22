package OneCoin.Server.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {MustHaveLimitOrMarketValidator.class})
public @interface MustHaveLimitOrMarket {
    String message() default "Limit과 Market 중 한 필드는 반드시 값이 입력되어야 합니다. 또한 한 번에 두 필드 모두 입력할 수 없습니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String limit();

    String market();
}
