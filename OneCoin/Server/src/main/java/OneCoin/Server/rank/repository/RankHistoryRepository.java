package OneCoin.Server.rank.repository;

import OneCoin.Server.rank.entity.RankHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RankHistoryRepository extends JpaRepository<RankHistory, Long> {
    Optional<RankHistory> findByUserId(Long userId);
}
