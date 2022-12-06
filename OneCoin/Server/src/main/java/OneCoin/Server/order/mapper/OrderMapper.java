package OneCoin.Server.order.mapper;

import OneCoin.Server.order.dto.OrderDto;
import OneCoin.Server.order.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "completedAmount", expression = "java(BigDecimal.ZERO)")
    @Mapping(target = "orderTime", expression = "java(java.time.LocalDateTime.now())")
    Order postDtoToOrder(OrderDto.Post postDto);

    List<OrderDto.GetResponse> orderToGetResponse(List<Order> orders);
}
