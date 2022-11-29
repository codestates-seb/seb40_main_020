package OneCoin.Server.order.repository;

import OneCoin.Server.coin.entity.Coin;
import OneCoin.Server.order.entity.TransactionHistory;
import OneCoin.Server.order.entity.enums.TransactionType;
import OneCoin.Server.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import OneCoin.Server.rank.dao.UserRoi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import java.time.LocalDateTime;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {
    Page<TransactionHistory> findByUserAndCreatedAtAfter(User user, LocalDateTime searchPeriod, Pageable pageable); // 기간 page
    Page<TransactionHistory> findByUserAndTransactionTypeAndCreatedAtAfter(User user, TransactionType transactionType, LocalDateTime searchPeriod, Pageable pageable); // 기간, 타입 page
    Page<TransactionHistory> findByUserAndCoinAndCreatedAtAfter(User user, Coin coin, LocalDateTime searchPeriod, Pageable pageable); // 기간, 코인 page
    Page<TransactionHistory> findByUserAndTransactionTypeAndCoinAndCreatedAtAfter(User user, TransactionType transactionType, Coin coin, LocalDateTime searchPeriod, Pageable pageable); // 기간, 타입, 코인 page
    List<TransactionHistory> findTop10ByUserAndCoinOrderByCreatedAtDesc(User user, Coin coin);

    @Query("SELECT " +
            "   new OneCoin.Server.rank.dao.UserRoi(t.user.id, t.user.displayName, SUM(t.settledAmount))" +
            "FROM " +
            "   TransactionHistory t " +
            "WHERE " +
            "   t.transactionType = 'BID' " +
            "GROUP BY " +
            "   t.user")
    List<UserRoi> findAllSumOfBidSettledAmount();

    @Query("SELECT " +
            "   new OneCoin.Server.rank.dao.UserRoi(t.user.id, SUM(t.settledAmount))" +
            "FROM " +
            "   TransactionHistory t " +
            "WHERE " +
            "   t.transactionType = 'ASK' " +
            "GROUP BY " +
            "   t.user")
    List<UserRoi> findAllSumOfAskSettledAmount();
}
