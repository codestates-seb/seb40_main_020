package OneCoin.Server.order.mapper;

import OneCoin.Server.order.dto.RedisOrderDto;
import OneCoin.Server.order.entity.RedisOrder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    RedisOrder redisPostDtoToRedisOrder(RedisOrderDto.Post redisPostDto);

    RedisOrderDto.PostResponse redisOrderToRedisPostResponse(RedisOrder redisOrder);
}
