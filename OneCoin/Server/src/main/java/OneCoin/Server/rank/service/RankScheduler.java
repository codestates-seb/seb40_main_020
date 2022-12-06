package OneCoin.Server.rank.service;

import OneCoin.Server.rank.dao.UserRoi;
import OneCoin.Server.rank.entity.Rank;
import OneCoin.Server.rank.mapper.RankMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class RankScheduler {
    private final RankService rankService;
    private final RankMapper mapper;

    @Scheduled(fixedDelay = 1, initialDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void updateTop10() {
        List<UserRoi> top10Roi = rankService.calculateTop10();
        if(top10Roi == null) {
            log.info("There is no transaction yet");
            return;
        }
        List<Rank> top10Entities = mapper.userRoisToRankEntities(top10Roi);
        rankService.update(top10Entities);
        String winnerName = "no one";
        if (top10Entities.size() != 0) {
            winnerName = top10Entities.get(0).getDisplayName();
        }
        log.info("Updating Top10, winner: {}, time: {}", winnerName, LocalDateTime.now());
    }
}
