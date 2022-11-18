package OneCoin.Server.order.controller;

import OneCoin.Server.dto.SingleResponseDto;
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

    @PostMapping("/{code}")
    public ResponseEntity postOrder(@PathVariable("code") String code,
                                    @Valid @RequestBody RedisOrderDto.Post redisPostDto) {
        RedisOrder mappingRedisOrder = mapper.redisPostDtoToRedisOrder(redisPostDto);
        RedisOrder redisOrder = orderService.createOrder(mappingRedisOrder, code);
        RedisOrderDto.PostResponse redisPostResponse = mapper.redisOrderToRedisPostResponse(redisOrder);

        return new ResponseEntity(new SingleResponseDto<>(redisPostResponse), HttpStatus.CREATED);
    }
}
