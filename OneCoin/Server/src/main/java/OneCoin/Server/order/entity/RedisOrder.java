package OneCoin.Server.order.entity;

import OneCoin.Server.coin.entity.Coin;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@RedisHash("order")
public class RedisOrder {

    @Id
    private Long orderId;

    private BigDecimal limit;

    private BigDecimal market;

    private BigDecimal stopLimit;

    @Column(nullable = false)
    private BigDecimal amount; // 미체결량

    private BigDecimal completedAmount; // 체결량

    private LocalDateTime orderTime = LocalDateTime.now();

    private boolean askOrBid;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private Long userId; // TODO USER로 매핑

    @ManyToOne
    @JoinColumn(name = "COIN_ID")
    private Coin coin;
}
