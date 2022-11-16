package OneCoin.Server.order.entity;

import OneCoin.Server.audit.CreatedOnlyAuditable;
import OneCoin.Server.coin.entity.Coin;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Getter
@Setter
@RedisHash("order")
public class RedisOrder extends CreatedOnlyAuditable {

    @Id
    private Long orderId;

    private BigDecimal limit;

    private BigDecimal market;

    private BigDecimal stopLimit;

    @Column(nullable = false)
    private BigDecimal amount;

    private boolean askOrBid;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private Long userId; // TODO USER로 매핑

    @ManyToOne
    @JoinColumn(name = "COIN_ID")
    private Coin coin;
}
