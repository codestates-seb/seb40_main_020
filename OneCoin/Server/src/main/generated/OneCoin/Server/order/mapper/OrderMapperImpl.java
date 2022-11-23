package OneCoin.Server.order.mapper;

import OneCoin.Server.order.dto.OrderDto;
import OneCoin.Server.order.entity.Order;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-11-23T18:08:15+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderDto.PostResponse redisOrderToRedisPostResponse(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderDto.PostResponse postResponse = new OrderDto.PostResponse();

        postResponse.setOrderId( order.getOrderId() );

        return postResponse;
    }
}
