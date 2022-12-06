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
    private Integer orderId;

    @Indexed
    private BigDecimal limit;

    private BigDecimal market;

    private BigDecimal stopLimit;

    private BigDecimal amount; // 미체결량

    private BigDecimal completedAmount; // 체결량

    private LocalDateTime orderTime;

    @Indexed
    private String orderType; // ASK, BID

    @Indexed
    private Long userId;

    @Indexed
    private String code;
}
