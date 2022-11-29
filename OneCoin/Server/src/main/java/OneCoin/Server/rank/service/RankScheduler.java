package OneCoin.Server.rank.service;

import OneCoin.Server.rank.dao.UserRoi;
import OneCoin.Server.rank.entity.RankEntity;
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

    @Scheduled(fixedDelay = 10, initialDelay = 1, timeUnit = TimeUnit.SECONDS)
    public void updateTop10() {
        List<UserRoi> top10Roi = rankService.calculateTop10();
        List<RankEntity> top10Entities = mapper.userRoisToRankEntities(top10Roi);
        rankService.update(top10Entities);
        String winnerName = top10Entities.get(0).getDisplayName();
        log.info("Updating Top10, winner: {}, time: {}", winnerName, LocalDateTime.now());
    }
}
