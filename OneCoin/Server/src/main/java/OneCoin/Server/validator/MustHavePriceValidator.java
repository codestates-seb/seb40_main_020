package OneCoin.Server.validator;

import OneCoin.Server.order.dto.OrderDto;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.List;

@Slf4j
public class MustHavePriceValidator implements ConstraintValidator<MustHavePrice, OrderDto.Post> {
    private String limit;
    private String market;
    private String stopLimit;

    @Override
    public void initialize(MustHavePrice constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        limit = constraintAnnotation.limit();
        market = constraintAnnotation.market();
        stopLimit = constraintAnnotation.stopLimit();
    }

    @Override
    public boolean isValid(OrderDto.Post orderPost, ConstraintValidatorContext context) {
        List<Double> prices = List.of(getFieldValue(orderPost, limit), getFieldValue(orderPost, market), getFieldValue(orderPost, stopLimit));

        if (getZeroCount(prices) == 3) { // 모든 필드 값이 없을 경우
            return false;
        } else if (getNotZeroCount(prices) != 1) { // 필드 값이 하나만 있지 않을 경우
            return false;
        }
        return true;
    }

    private double getFieldValue(OrderDto.Post orderPost, String fieldName) { // reflection
        Class<?> clazz = orderPost.getClass();
        Field orderField;
        try {
            orderField = clazz.getDeclaredField(fieldName);
            orderField.setAccessible(true);
            Object target = orderField.get(orderPost);
            if (!(target instanceof Double)) {
                throw new ClassCastException();
            }
            return (double) target;
        } catch (NoSuchFieldException e) {
            log.error("NoSuchFieldException", e);
        } catch (IllegalAccessException e) {
            log.error("IllegalAccessException", e);
        }
        throw new RuntimeException("Not Found Field");
    }

    private int getZeroCount(List<Double> prices) {
        int cnt = 0;
        for (double price : prices) {
            if (price == 0) {
                cnt++;
            }
        }
        return cnt;
    }

    private int getNotZeroCount(List<Double> prices) {
        int cnt = 0;
        for (double price : prices) {
            if (price != 0) {
                cnt++;
            }
        }
        return cnt;
    }
}
