package OneCoin.Server.order.service;

import OneCoin.Server.coin.entity.Coin;
import OneCoin.Server.coin.service.CoinService;
import OneCoin.Server.config.auth.utils.LoggedInUserInfoUtils;
import OneCoin.Server.deposit.entity.Deposit;
import OneCoin.Server.exception.BusinessLogicException;
import OneCoin.Server.exception.ExceptionCode;
import OneCoin.Server.order.entity.Order;
import OneCoin.Server.order.entity.TransactionHistory;
import OneCoin.Server.order.entity.enums.Period;
import OneCoin.Server.order.entity.enums.TransactionType;
import OneCoin.Server.order.mapper.TransactionHistoryMapper;
import OneCoin.Server.order.repository.TransactionHistoryRepository;
import OneCoin.Server.rank.repository.RankHistoryRepository;
import OneCoin.Server.rank.service.RankService;
import OneCoin.Server.swap.entity.Swap;
import OneCoin.Server.user.entity.User;
import OneCoin.Server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionHistoryService {
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final TransactionHistoryMapper mapper;
    private final UserService userService;
    private final CoinService coinService;
    private final LoggedInUserInfoUtils loggedInUserInfoUtils;
    private final String defaultType = "ALL";
    private final RankService rankService;

    @Async("upbitExecutor")
    public void createTransactionHistoryByOrder(Order order) {
        User user = userService.findVerifiedUser(order.getUserId());
        Coin coin = coinService.findCoin(order.getCode());
        TransactionHistory transactionHistory = mapper.orderToTransactionHistory(order, user, coin);

        transactionHistoryRepository.save(transactionHistory);

        rankService.accumulateTransaction(transactionHistory);
    }

    public void createTransactionHistoryBySwap(Swap swap) {
        TransactionHistory transactionHistory = mapper.swapToTransactionHistory(swap);

        transactionHistoryRepository.save(transactionHistory);
    }

    public void createTransactionHistoryByDeposit(Deposit deposit) {
        TransactionHistory transactionHistory = mapper.depositToTransactionHistory(deposit);

        transactionHistoryRepository.save(transactionHistory);
    }

    @Transactional(readOnly = true)
    public Page<TransactionHistory> findTransactionHistory(String period, String type, String code, PageRequest pageRequest) {
        User user = loggedInUserInfoUtils.extractUser();
        LocalDateTime searchPeriod = getSearchPeriod(period);

        if (code == null && !type.equals(defaultType)) {
            TransactionType transactionType = getTransactionType(type);
            return transactionHistoryRepository.findByUserAndTransactionTypeAndCreatedAtAfter(user, transactionType, searchPeriod, pageRequest);
        }
        if (code != null && type.equals(defaultType)) {
            Coin coin = coinService.findCoin(code);
            return transactionHistoryRepository.findByUserAndCoinAndCreatedAtAfter(user, coin, searchPeriod, pageRequest);
        }
        if (code != null) {
            TransactionType transactionType = getTransactionType(type);
            Coin coin = coinService.findCoin(code);
            return transactionHistoryRepository.findByUserAndTransactionTypeAndCoinAndCreatedAtAfter(user, transactionType, coin, searchPeriod, pageRequest);
        }
        return transactionHistoryRepository.findByUserAndCreatedAtAfter(user, searchPeriod, pageRequest);
    }

    private LocalDateTime getSearchPeriod(String period) {
        LocalDateTime searchPeriod = LocalDateTime.now();

        if (period.equals(Period.WEEK.getAbbreviation())) {
            return searchPeriod.minusWeeks(1);
        } else if (period.equals(Period.MONTH.getAbbreviation())) {
            return searchPeriod.minusMonths(1);
        } else if (period.equals(Period.THREE_MONTHS.getAbbreviation())) {
            return searchPeriod.minusMonths(3);
        } else if (period.equals(Period.SIX_MONTHS.getAbbreviation())) {
            return searchPeriod.minusMonths(6);
        } else {
            throw new BusinessLogicException(ExceptionCode.NOT_CORRECT_PERIOD);
        }
    }

    private TransactionType getTransactionType(String type) {
        if (type.equals(TransactionType.BID.getType())) {
            return TransactionType.BID;
        } else if (type.equals(TransactionType.ASK.getType())) {
            return TransactionType.ASK;
        } else if (type.equals(TransactionType.DEPOSIT.getType())) {
            return TransactionType.DEPOSIT;
        } else if (type.equals(TransactionType.SWAP.getType())) {
            return TransactionType.SWAP;
        } else {
            throw new BusinessLogicException(ExceptionCode.NOT_CORRECT_TYPE);
        }
    }

    public List<TransactionHistory> findTransactionHistoryByCoin(String code) {
        User user = loggedInUserInfoUtils.extractUser();
        Coin coin = coinService.findCoin(code);

        return getTop10OrderTransactionHistories(user, coin);
    }

    private List<TransactionHistory> getTop10OrderTransactionHistories(User user, Coin coin) {
        List<TransactionHistory> transactionHistories = transactionHistoryRepository.findTop10ByUserAndCoinAndTransactionTypeOrderByCreatedAtDesc(user, coin, TransactionType.BID);
        transactionHistories.addAll(transactionHistoryRepository.findTop10ByUserAndCoinAndTransactionTypeOrderByCreatedAtDesc(user, coin, TransactionType.ASK));
        transactionHistories.sort(Comparator.comparing(TransactionHistory::getCreatedAt).reversed());
        if (transactionHistories.size() > 10) {
            return transactionHistories.subList(0, 9);
        }
        return transactionHistories;
    }
}
