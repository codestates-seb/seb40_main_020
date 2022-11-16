package OneCoin.Server.coin.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity
public class Coin {

    @Id
    private Long coinId;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String coinName;
}
