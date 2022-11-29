package OneCoin.Server.rank.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RankEntity {
    @Id
    private Integer rank;
    private String displayName;
    private String ROI;
}
