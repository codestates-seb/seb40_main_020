package OneCoin.Server.order.service;

import OneCoin.Server.exception.BusinessLogicException;
import OneCoin.Server.exception.ExceptionCode;
import OneCoin.Server.order.entity.Order;
import OneCoin.Server.order.entity.Wallet;
import OneCoin.Server.order.mapper.WalletMapper;
import OneCoin.Server.order.repository.OrderRepository;
import OneCoin.Server.order.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final OrderRepository orderRepository;
    private final WalletMapper mapper;

    public void createWallet(List<Order> orders, BigDecimal tradeVolume) {
        List<Wallet> wallets = new ArrayList<>();

        for (Order order : orders) {
            BigDecimal completedAmount = getCompletedAmount(order, tradeVolume);
            Wallet findWallet = findMyWallet(order.getUserId(), order.getCode());
            if (findWallet != null) { // 지갑에 이미 존재할 때
                updateWalletByBid(findWallet, order.getLimit(), completedAmount);
                continue;
            }

            Wallet wallet = mapper.bidOrderToNewWallet(order, completedAmount);
            wallets.add(wallet);
        }
        walletRepository.saveAll(wallets);
    }

    private BigDecimal getCompletedAmount(Order order, BigDecimal tradeVolume) {
        BigDecimal orderAmount = order.getAmount();

        int comparison = orderAmount.compareTo(tradeVolume);
        if (comparison <= 0) { // 전부 체결된 주문
            deleteCompletedOrder(order);
            return orderAmount;
        }
        saveRemainingAmount(order, tradeVolume);
        return tradeVolume;
    }

    private void saveRemainingAmount(Order order, BigDecimal completedAmount) {
        order.setAmount(order.getAmount().subtract(completedAmount));
        order.setCompletedAmount(order.getCompletedAmount().add(completedAmount));
        orderRepository.save(order);
    }

    private void deleteCompletedOrder(Order order) {
        // TODO Transaction History 저장 (비동기)
        orderRepository.delete(order);
    }

    private void updateWalletByBid(Wallet wallet, BigDecimal orderPrice, BigDecimal completedAmount) {
        Wallet updatedWallet = mapper.bidOrderToUpdatedWallet(wallet, orderPrice, completedAmount);
        walletRepository.save(updatedWallet);
    }

    public Wallet findVerifiedWalletWithCoin(long userId, String code) {
        Wallet wallet = findMyWallet(userId, code);
        if (wallet == null) {
            throw new BusinessLogicException(ExceptionCode.HAVE_NO_COIN);
        }
        return wallet;
    }

    private Wallet findMyWallet(long userId, String code) {
        return walletRepository.findByUserIdAndCode(userId, code);
    }
}
