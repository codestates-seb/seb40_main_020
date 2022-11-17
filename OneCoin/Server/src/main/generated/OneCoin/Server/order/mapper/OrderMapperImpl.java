package OneCoin.Server.order.mapper;

import OneCoin.Server.order.dto.RedisOrderDto;
import OneCoin.Server.order.entity.RedisOrder;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-11-17T19:45:19+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 11.0.16 (Azul Systems, Inc.)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public RedisOrderDto.PostResponse redisOrderToRedisPostResponse(RedisOrder redisOrder) {
        if ( redisOrder == null ) {
            return null;
        }

        RedisOrderDto.PostResponse postResponse = new RedisOrderDto.PostResponse();

        postResponse.setOrderId( redisOrder.getOrderId() );

        return postResponse;
    }
}
