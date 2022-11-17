package OneCoin.Server.coin.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Coin {

    @Id
    private Long coinId;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String coinName;
}
