package OneCoin.Server.rank.dto;

import OneCoin.Server.rank.entity.RankEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class RankDto {
    private String referenceDate;
    private List<RankEntity> users;
}
