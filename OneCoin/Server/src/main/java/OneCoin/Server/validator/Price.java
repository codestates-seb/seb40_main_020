package OneCoin.Server.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {PriceValidator.class})
public @interface Price {
    String message() default "음수 값은 허용하지 않습니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
