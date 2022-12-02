package OneCoin.Server.swap.repository;

import OneCoin.Server.swap.entity.Swap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SwapRepository extends JpaRepository<Swap, Long> {
}
