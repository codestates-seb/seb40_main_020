package OneCoin.Server.order.controller;

import OneCoin.Server.order.dto.RedisOrderDto;
import OneCoin.Server.order.entity.RedisOrder;
import OneCoin.Server.order.mapper.OrderMapper;
import OneCoin.Server.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper mapper;

    @PostMapping("/ask")
    public ResponseEntity postAskOrder(@Valid @RequestBody RedisOrderDto.Post redisPostDto) {
        RedisOrder redisOrder = mapper.redisPostDtoToRedisOrder(redisPostDto);
        redisOrder.setAskOrBid(true);
        orderService.createOrder(redisOrder);
        RedisOrderDto.PostResponse redisPostResponse = mapper.redisOrderToRedisPostResponse(redisOrder);
        return new ResponseEntity(redisPostResponse, HttpStatus.CREATED);
    }
}
