package OneCoin.Server.order.repository;

import OneCoin.Server.order.entity.TransactionHistory;
import OneCoin.Server.order.entity.enums.TransactionType;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {
    @Query("SELECT " +
            "   SUM(t.settledAmount) " +
            "FROM " +
            "   TransactionHistory t " +
            "WHERE " +
            "   t.transactionType = :type " +
            "GROUP BY " +
            "   t.user.id " +
            "HAVING " +
            "   t.user.id = :userId")
    BigDecimal sumSettledAmountByTypeAndUserId(@Param("userId") Long userId, @Param("type") TransactionType type);
}