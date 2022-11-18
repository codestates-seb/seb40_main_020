package OneCoin.Server.order.mapper;

import OneCoin.Server.order.dto.RedisOrderDto;
import OneCoin.Server.order.entity.RedisOrder;
import org.mapstruct.Mapper;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    default RedisOrder redisPostDtoToRedisOrder(RedisOrderDto.Post redisPostDto) {
        if (redisPostDto == null) {
            return null;
        }

        return RedisOrder.builder()
                        .limit(new BigDecimal(String.valueOf(redisPostDto.getLimit())))
                        .market(new BigDecimal(String.valueOf(redisPostDto.getMarket())))
                        .stopLimit(new BigDecimal(String.valueOf(redisPostDto.getStopLimit())))
                        .amount(new BigDecimal(String.valueOf(redisPostDto.getAmount())))
                        .askOrBid(redisPostDto.getAskOrBid() != 0)
                        .build();
    }

    RedisOrderDto.PostResponse redisOrderToRedisPostResponse(RedisOrder redisOrder);
}
