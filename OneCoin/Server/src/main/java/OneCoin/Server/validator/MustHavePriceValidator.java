package OneCoin.Server.validator;

import OneCoin.Server.order.dto.OrderDto;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

@Slf4j
public class MustHavePriceValidator implements ConstraintValidator<MustHavePrice, OrderDto.Post> {
    private String limit;
    private String market;

    @Override
    public void initialize(MustHavePrice constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        limit = constraintAnnotation.limit();
        market = constraintAnnotation.market();
    }

    @Override
    public boolean isValid(OrderDto.Post orderPost, ConstraintValidatorContext context) {
        double limitPrice = Double.parseDouble(getFieldValue(orderPost, limit)) ;
        double marketPrice = Double.parseDouble(getFieldValue(orderPost, market));

        if (limitPrice == 0 && marketPrice == 0) { // 두 필드 모두 값이 없을 경우
            return false;
        } else if (limitPrice != 0 && marketPrice != 0) { // 필드 값이 하나만 있지 않을 경우
            return false;
        }
        return true;
    }

    private String getFieldValue(OrderDto.Post orderPost, String fieldName) { // reflection
        Class<?> clazz = orderPost.getClass();
        Field orderField;
        try {
            orderField = clazz.getDeclaredField(fieldName);
            orderField.setAccessible(true);
            Object target = orderField.get(orderPost);
            if (!(target instanceof String)) {
                throw new ClassCastException();
            }
            return (String) target;
        } catch (NoSuchFieldException e) {
            log.error("NoSuchFieldException", e);
        } catch (IllegalAccessException e) {
            log.error("IllegalAccessException", e);
        }
        throw new RuntimeException("Not Found Field");
    }
}
