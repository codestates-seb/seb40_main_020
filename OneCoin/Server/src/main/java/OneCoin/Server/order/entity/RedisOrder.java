package OneCoin.Server.order.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("order")
public class RedisOrder {

    @Id
    private Long orderId;

    @Indexed
    private BigDecimal limit;

    @Indexed
    private BigDecimal market;

    @Indexed
    private BigDecimal stopLimit;

    @Column(nullable = false)
    private BigDecimal amount; // 미체결량

    private LocalDateTime orderTime = LocalDateTime.now();

    @Indexed
    private boolean askBid; // (ask:True, bid:False)

    @Indexed
    private Long userId;

    @Indexed
    private String code;
}
