package OneCoin.Server.coin.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Coin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long coinId;

    @Column(nullable = false, unique = true, length = 20)
    private String code;

    @Column(nullable = false, unique = true, length = 20)
    private String coinName;
}
