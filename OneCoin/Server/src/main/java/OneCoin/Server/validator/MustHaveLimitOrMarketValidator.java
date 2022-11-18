package OneCoin.Server.validator;

import OneCoin.Server.order.dto.RedisOrderDto;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

@Slf4j
public class MustHaveLimitOrMarketValidator implements ConstraintValidator<MustHaveLimitOrMarket, RedisOrderDto.Post> {
    private String limit;
    private String  market;

    @Override
    public void initialize(MustHaveLimitOrMarket constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        limit = constraintAnnotation.limit();
        market = constraintAnnotation.market();
    }

    @Override
    public boolean isValid(RedisOrderDto.Post redisOrderPost, ConstraintValidatorContext context) {
        double limitOrder = getFieldValue(redisOrderPost, limit);
        double marketOrder = getFieldValue(redisOrderPost, market);
        if ((limitOrder == 0 && marketOrder == 0) || // 두 필드 모두 값이 없을 경우
                (limitOrder != 0 && marketOrder != 0)) { // 두 필드 모두 값이 있을 경우
            return false;
        }
        return true;
    }

    private double getFieldValue(RedisOrderDto.Post redisOrderPost, String fieldName) { // reflection
        Class<?> clazz = redisOrderPost.getClass();
        Field orderField;
        try {
            orderField = clazz.getDeclaredField(fieldName);
            orderField.setAccessible(true);
            Object target = orderField.get(redisOrderPost);
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
}
