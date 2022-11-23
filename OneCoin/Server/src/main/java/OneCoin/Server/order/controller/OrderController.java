package OneCoin.Server.order.controller;

import OneCoin.Server.dto.MultiResponseDto;
import OneCoin.Server.dto.SingleResponseDto;
import OneCoin.Server.order.dto.OrderDto;
import OneCoin.Server.order.entity.Order;
import OneCoin.Server.order.mapper.OrderMapper;
import OneCoin.Server.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper mapper;

    @PostMapping("/{code}")
    public ResponseEntity postOrder(@PathVariable("code") String code,
                                    @Valid @RequestBody OrderDto.Post redisPostDto) {
        Order mappingOrder = mapper.redisPostDtoToRedisOrder(redisPostDto);
        Order order = orderService.createOrder(mappingOrder, code);
        OrderDto.PostResponse redisPostResponse = mapper.redisOrderToRedisPostResponse(order);

        return new ResponseEntity(new SingleResponseDto<>(redisPostResponse), HttpStatus.CREATED);
    }

    @GetMapping("/completion")
    public ResponseEntity getCompletedOrder(@RequestParam("code") String code) { // 체결된 내역
        // TODO
        // transaction history에서 봐야함
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/non-trading")
    public ResponseEntity getNonTradingOrder(@RequestParam("code") String code) {
        List<Order> orders = orderService.findOrders(code);
        return new ResponseEntity(new MultiResponseDto<>(orders), HttpStatus.OK);
    }

    @DeleteMapping("/non-trading/{order-id}")
    public ResponseEntity deleteNonTradingOrder(@PathVariable("order-id") long orderId) {
        orderService.cancelOrder(orderId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
