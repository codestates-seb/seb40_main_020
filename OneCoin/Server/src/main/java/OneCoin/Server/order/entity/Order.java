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

    @Indexed
    private BigDecimal market;

    @Indexed
    private BigDecimal stopLimit;

    private BigDecimal amount; // 미체결량

    private BigDecimal commission;

    private LocalDateTime orderTime = LocalDateTime.now();

    @Indexed
    private String orderType; // ASK, BID

    @Indexed
    private Long userId;

    @Indexed
    private String code;
}
