package OneCoin.Server.rank.repository;

import OneCoin.Server.rank.entity.RankEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankRepository extends JpaRepository<RankEntity, Integer> {
}
