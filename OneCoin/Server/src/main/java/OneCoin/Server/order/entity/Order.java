package OneCoin.Server.order.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("order")
public class Order {

    @Id
    private Long orderId;

    @Indexed
    private BigDecimal limit;

    private BigDecimal market;

    private BigDecimal stopLimit;

    private BigDecimal amount; // 미체결량

    private BigDecimal completedAmount; // 체결량 -> transaction history에서 수수료 계산할 때 필요

    private LocalDateTime orderTime;

    @Indexed
    private String orderType; // ASK, BID

    @Indexed
    private Long userId;

    @Indexed
    private String code;
}
