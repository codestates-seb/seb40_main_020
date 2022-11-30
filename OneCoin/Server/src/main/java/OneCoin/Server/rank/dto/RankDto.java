package OneCoin.Server.rank.dto;

import OneCoin.Server.rank.entity.Rank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class RankDto {
    private String referenceDate;
    private List<Rank> users;
}
