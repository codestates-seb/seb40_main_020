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
@RedisHash("askOrder")
public class AskOrder extends CreatedOnlyAuditable {

    @Id
    private Long askOrderId;

    private BigDecimal limit;

    private BigDecimal market;

    private BigDecimal stopLimit;

    @Column(nullable = false)
    private BigDecimal amount;

    // TODO 연관관계 매핑
}
