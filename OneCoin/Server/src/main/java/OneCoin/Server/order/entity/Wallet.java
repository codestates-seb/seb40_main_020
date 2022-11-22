package OneCoin.Server.order.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.math.BigDecimal;

@Getter
@Setter
@RedisHash("wallet")
public class Wallet {

    @Id
    private Long walletId;

    private BigDecimal amount;

    private BigDecimal averagePrice;

    private String change; // RISE, EVEN, FALL

    private BigDecimal changePrice;

    private BigDecimal changeRate;

    @Indexed
    private Long userId;

    @Indexed
    private String code;
}
