package OneCoin.Server.order.mapper;

import OneCoin.Server.order.dto.OrderDto;
import OneCoin.Server.order.entity.Order;
import org.mapstruct.Mapper;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    default Order redisPostDtoToRedisOrder(OrderDto.Post redisPostDto) {
        if (redisPostDto == null) {
            return null;
        }

        return Order.builder()
                        .limit(new BigDecimal(String.valueOf(redisPostDto.getLimit())))
                        .market(new BigDecimal(String.valueOf(redisPostDto.getMarket())))
                        .stopLimit(new BigDecimal(String.valueOf(redisPostDto.getStopLimit())))
                        .amount(new BigDecimal(String.valueOf(redisPostDto.getAmount())))
                        .askBid(redisPostDto.getAskBid() != 0)
                        .build();
    }

    OrderDto.PostResponse redisOrderToRedisPostResponse(Order order);
}
