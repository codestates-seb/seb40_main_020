package OneCoin.Server.deposit.entity;

import OneCoin.Server.audit.CreatedOnlyAuditable;
import OneCoin.Server.balance.entity.Balance;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Deposit extends CreatedOnlyAuditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long depositId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deposit_balance_id")
    private Balance balance;

    @Column(nullable = false)
    private Long depositAmount;
}
