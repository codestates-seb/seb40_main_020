package OneCoin.Server.rank.repository;

import OneCoin.Server.rank.entity.Rank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankRepository extends JpaRepository<Rank, Integer> {
}
