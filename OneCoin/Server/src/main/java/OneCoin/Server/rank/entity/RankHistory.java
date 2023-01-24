package OneCoin.Server.rank.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RankHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long rankHistoryId;
    private long userId;
    private BigDecimal accumulatedBid;
    private BigDecimal accumulatedAsk;
}
