package OneCoin.Server.swap.entity;

import OneCoin.Server.audit.CreatedOnlyAuditable;
import OneCoin.Server.coin.entity.Coin;
import OneCoin.Server.user.entity.User;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Swap extends CreatedOnlyAuditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long swapId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "swap_user_id", updatable = false, nullable = false)
    private User user;

    // 현재 가지고 있는 코인
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "swap_given_coin_id", updatable = false, nullable = false)
    private Coin givenCoin;

    // 얻고 싶은 코인
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "swap_taken_coin_id", updatable = false, nullable = false)
    private Coin takenCoin;

    @Column(nullable = false, updatable = false, scale = 15, precision = 30)
    private BigDecimal givenAmount;

    @Column(nullable = false, updatable = false, scale = 15, precision = 30)
    private BigDecimal takenAmount;

    // 수수료, 스왑 시 풀에 제공하는 토큰의 양(실제 거래량 = 스왑 요청량 - 수수료)
    @Column(nullable = false, updatable = false, scale = 15, precision = 30)
    private BigDecimal commission;

    // 스왑 당시 가격
    @Column(nullable = false, updatable = false, scale = 15, precision = 30)
    private BigDecimal givenCoinPrice;

    @Column(nullable = false, updatable = false, scale = 15, precision = 30)
    private BigDecimal takenCoinPrice;
}
