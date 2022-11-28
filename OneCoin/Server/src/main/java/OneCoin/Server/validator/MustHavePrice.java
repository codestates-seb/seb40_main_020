package OneCoin.Server.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {MustHavePriceValidator.class})
public @interface MustHavePrice {
    String message() default "limit과 market 중 하나의 필드는 반드시 값이 입력되어야 합니다. 또한 두 필드를 동시에 입력할 수 없습니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String limit();

    String market();
}
