package OneCoin.Server.rank.dto;

import OneCoin.Server.rank.entity.RankEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class RankDto {
    private LocalDateTime referenceDate;
    private List<RankEntity> users;
}
