package OneCoin.Server.order.service;

import OneCoin.Server.coin.entity.Coin;
import OneCoin.Server.coin.service.CoinService;
import OneCoin.Server.order.entity.Order;
import OneCoin.Server.order.entity.TransactionHistory;
import OneCoin.Server.order.mapper.TransactionHistoryOrderMapper;
import OneCoin.Server.order.repository.TransactionHistoryRepository;
import OneCoin.Server.user.entity.User;
import OneCoin.Server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionHistoryService {
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final TransactionHistoryOrderMapper mapper;
    private final UserService userService;
    private final CoinService coinService;

//    @Async
    public void createTransactionHistory(Order order) {
        User user = userService.findVerifiedUser(order.getUserId());
        Coin coin = coinService.findCoin(order.getCode());
        TransactionHistory transactionHistory = mapper.orderToTransactionHistory(order, user, coin);

        transactionHistoryRepository.save(transactionHistory);
    }

    public void sumAllBids(Long userId) {
        transactionHistoryRepository.findAll();
    }

    public void sumAllAsks(Long userId) {

    }

}
