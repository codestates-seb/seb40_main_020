package OneCoin.Server.rank.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "RANKS")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rank {
    @Id
    @Column(name = "ranks")
    private Integer rank;
    private String displayName;
    private String ROI;
}
