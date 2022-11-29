package OneCoin.Server.order.entity;

import OneCoin.Server.audit.CreatedOnlyAuditable;
import OneCoin.Server.coin.entity.Coin;
import OneCoin.Server.order.entity.enums.TransactionType;
import OneCoin.Server.user.entity.User;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistory extends CreatedOnlyAuditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long TransactionHistoryId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10, updatable = false)
    private TransactionType transactionType;

    @Column(nullable = false, updatable = false)
    private BigDecimal amount; // 수량

    @Column(nullable = false, updatable = false)
    private BigDecimal price; // 가격

    @Column(nullable = false, updatable = false)
    private BigDecimal totalAmount; // 총 거래 금액

    @Column(nullable = false, updatable = false)
    private double commission; // 수수료, 소수점 둘째 자리에서 반올림

    @Column(nullable = false, updatable = false)
    private BigDecimal settledAmount; // 정산 금액

    @Column(nullable = false, updatable = false)
    private LocalDateTime orderTime; // 주문 시간, ASK와 BID에서 사용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coin_id", updatable = false)
    private Coin coin;
}