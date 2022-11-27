package OneCoin.Server.order.mapper;

import OneCoin.Server.order.dto.OrderDto;
import OneCoin.Server.order.entity.Order;
import org.mapstruct.Mapper;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    default Order postDtoToOrder(OrderDto.Post postDto) {
        if (postDto == null) {
            return null;
        }

        return Order.builder()
                .limit(new BigDecimal(String.valueOf(postDto.getLimit())))
                .market(new BigDecimal(String.valueOf(postDto.getMarket())))
                .stopLimit(new BigDecimal(String.valueOf(postDto.getStopLimit())))
                .amount(new BigDecimal(String.valueOf(postDto.getAmount())))
                .orderType(postDto.getOrderType())
                .build();
    }

    List<OrderDto.GetResponse> orderToGetResponse(List<Order> orders);
}
