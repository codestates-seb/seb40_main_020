package OneCoin.Server.order.entity;

import OneCoin.Server.coin.entity.Coin;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

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

    private BigDecimal limit;

    private BigDecimal market;

    private BigDecimal stopLimit;

    @Column(nullable = false)
    private BigDecimal amount; // 미체결량

    private LocalDateTime orderTime = LocalDateTime.now();

    private boolean askOrBid; // (ask:True, bid:False)

    private Long userId; // TODO USER로 변경

    private Coin coin;
}
