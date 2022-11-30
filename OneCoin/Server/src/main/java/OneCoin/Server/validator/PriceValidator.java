package OneCoin.Server.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PriceValidator implements ConstraintValidator<Price, String> {
    @Override
    public void initialize(Price constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Double.parseDouble(value) >= 0;
    }
}
