package OneCoin.Server.order.service;

import OneCoin.Server.coin.entity.Coin;
import OneCoin.Server.coin.service.CoinService;
import OneCoin.Server.config.auth.utils.LoggedInUserInfoUtils;
import OneCoin.Server.exception.BusinessLogicException;
import OneCoin.Server.exception.ExceptionCode;
import OneCoin.Server.order.entity.Order;
import OneCoin.Server.order.entity.TransactionHistory;
import OneCoin.Server.order.entity.enums.Period;
import OneCoin.Server.order.entity.enums.TransactionType;
import OneCoin.Server.order.mapper.TransactionHistoryOrderMapper;
import OneCoin.Server.order.repository.TransactionHistoryRepository;
import OneCoin.Server.user.entity.User;
import OneCoin.Server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionHistoryService {
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final TransactionHistoryOrderMapper mapper;
    private final UserService userService;
    private final CoinService coinService;
    private final LoggedInUserInfoUtils loggedInUserInfoUtils;
    private final String defaultType = "ALL";

    // @Async
    public void createTransactionHistory(Order order) {
        User user = userService.findVerifiedUser(order.getUserId());
        Coin coin = coinService.findCoin(order.getCode());
        TransactionHistory transactionHistory = mapper.orderToTransactionHistory(order, user, coin);

        transactionHistoryRepository.save(transactionHistory);
    }

    @Transactional(readOnly = true)
    public Page<TransactionHistory> findTransactionHistory(String period, String type, String code, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        LocalDateTime searchPeriod = getSearchPeriod(period);
        User user = loggedInUserInfoUtils.extractUser();

        if (code.isEmpty() && !type.equals(defaultType)) { // 타입 지정
            TransactionType transactionType = getTransactionType(type);
            return transactionHistoryRepository.findByUserAndTransactionTypeAndCreatedAtAfter(user, transactionType, searchPeriod, pageRequest);
        }
        if (!code.isEmpty() && type.equals(defaultType)) { // 코인 지정
            Coin coin = coinService.findCoin(code);
            return transactionHistoryRepository.findByUserAndCoinAndCreatedAtAfter(user, coin, searchPeriod, pageRequest);
        }
        if (!code.isEmpty() && !type.equals(defaultType)) { // 타입, 코인 지정
            TransactionType transactionType = getTransactionType(type);
            Coin coin = coinService.findCoin(code);
            return transactionHistoryRepository.findByUserAndTransactionTypeAndCoinAndCreatedAtAfter(user, transactionType, coin, searchPeriod, pageRequest);
        }
        return transactionHistoryRepository.findByUserAndCreatedAtAfter(user, searchPeriod, pageRequest); // 지정 x
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
        return transactionHistoryRepository.findTop10ByUserAndCoinOrderByCreatedAtDesc(user, coin);
    }

    public void sumAllBids(Long userId) {
        transactionHistoryRepository.findAll();
    }

    public void sumAllAsks(Long userId) {

    }

}
