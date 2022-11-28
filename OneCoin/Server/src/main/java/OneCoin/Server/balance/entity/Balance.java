package OneCoin.Server.balance.entity;

import OneCoin.Server.audit.Auditable;
import OneCoin.Server.deposit.entity.Deposit;
import OneCoin.Server.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Balance extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long balanceId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "balance_user_id")
    private User user;

    @OneToMany(mappedBy = "balance", cascade = CascadeType.REMOVE)
    private List<Deposit> deposit;

    @Column(nullable = false)
    private BigDecimal balance;
}
