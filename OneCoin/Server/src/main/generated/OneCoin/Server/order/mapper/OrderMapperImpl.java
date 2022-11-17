package OneCoin.Server.order.mapper;

import OneCoin.Server.order.dto.RedisOrderDto;
import OneCoin.Server.order.entity.RedisOrder;
import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-11-16T19:10:44+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 11.0.16 (Azul Systems, Inc.)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public RedisOrder redisPostDtoToRedisOrder(RedisOrderDto.Post redisPostDto) {
        if ( redisPostDto == null ) {
            return null;
        }

        RedisOrder redisOrder = new RedisOrder();

        if ( redisPostDto.getLimit() != null ) {
            redisOrder.setLimit( new BigDecimal( redisPostDto.getLimit() ) );
        }
        if ( redisPostDto.getMarket() != null ) {
            redisOrder.setMarket( new BigDecimal( redisPostDto.getMarket() ) );
        }
        if ( redisPostDto.getStopLimit() != null ) {
            redisOrder.setStopLimit( new BigDecimal( redisPostDto.getStopLimit() ) );
        }
        if ( redisPostDto.getAmount() != null ) {
            redisOrder.setAmount( new BigDecimal( redisPostDto.getAmount() ) );
        }

        return redisOrder;
    }

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
