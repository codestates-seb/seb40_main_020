package OneCoin.Server.order.entity;

import OneCoin.Server.audit.CreatedOnlyAuditable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Column;
import java.math.BigDecimal;

@Getter
@Setter
@RedisHash("bidOrder")
public class BidOrder extends CreatedOnlyAuditable {

    @Id
    private Long bidOrderId;

    private BigDecimal limit;

    private BigDecimal market;

    private BigDecimal stopLimit;

    @Column(nullable = false)
    private BigDecimal amount;

    // TODO 연관관계 매핑
}
