package OneCoin.Server.rank.mapper;

import OneCoin.Server.rank.dao.UserRoi;
import OneCoin.Server.rank.dto.RankDto;
import OneCoin.Server.rank.entity.RankEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class RankMapper {
    public RankDto userRoisToRankDtos(List<RankEntity> userRois) {
        RankDto rankDto = RankDto.builder()
                .referenceDate(LocalDateTime.now().toString())
                .users(userRois)
                .build();
        return rankDto;
    }

    public List<RankEntity> userRoisToRankEntities(List<UserRoi> userRois) {
        List<RankEntity> users = new ArrayList<>();
        for (int i = 0; i < userRois.size(); i++) {
            UserRoi userRoi = userRois.get(i);
            String roi = String.format("%.2f", userRoi.getTotalRoi() * 100.0) + "%";
            RankEntity dto = RankEntity.builder()
                    .rank(i + 1)
                    .displayName(userRoi.getUserDisplayName())
                    .ROI(roi)
                    .build();
            users.add(dto);
        }
        return users;
    }
}