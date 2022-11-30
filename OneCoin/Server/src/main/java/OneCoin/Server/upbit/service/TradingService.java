package OneCoin.Server.upbit.service;

import OneCoin.Server.order.entity.Order;
import OneCoin.Server.order.entity.Wallet;
import OneCoin.Server.order.entity.enums.TransactionType;
import OneCoin.Server.order.repository.OrderRepository;
import OneCoin.Server.order.service.WalletService;
import OneCoin.Server.upbit.entity.Trade;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TradingService {

    private final OrderRepository orderRepository;
    private final WalletService walletService;

    @EventListener
    @Async("upbitExecutor")
    public void completeOrders(Trade trade) {
        BigDecimal tradePrice = new BigDecimal(trade.getTradePrice());
        BigDecimal tradeVolume = new BigDecimal(trade.getTradeVolume());
        String orderType = trade.getOrderType();

        List<Order> orders = orderRepository.findAllByOrderTypeAndCode(orderType, trade.getCode());
        if (orders.isEmpty()) {
            return;
        }

        if (orderType.equals(TransactionType.BID.getType())) {
            orders.removeIf(order -> order.getLimit().compareTo(tradePrice) < 0); // 매수 시 실제 체결 가격보다 크거나 같은 가격만 체결
            if (orders.isEmpty()) {
                return;
            }
            tradeBid(orders, tradeVolume);
        }
        if (orderType.equals(TransactionType.ASK.getType())) {
            orders.removeIf(order -> order.getLimit().compareTo(tradePrice) > 0); // 매도 시 실제 체결 가격보다 작거나 같은 가격만 체결
            if (orders.isEmpty()) {
                return;
            }
            tradeAsk(orders, tradeVolume);
        }
    }

    private void tradeBid(List<Order> orders, BigDecimal tradeVolume) {
        for (Order order : orders) {
            Wallet findWallet = walletService.findMyWallet(order.getUserId(), order.getCode());

            if (findWallet != null) { // 지갑에 이미 존재할 때
                walletService.updateWalletByBid(findWallet, order, tradeVolume);
            } else {
                walletService.createWallet(order, tradeVolume);
            }
        }
    }

    private void tradeAsk(List<Order> orders, BigDecimal tradeVolume) {
        for (Order order : orders) {
            Wallet findWallet = walletService.findMyWallet(order.getUserId(), order.getCode());
            walletService.updateWalletByAsk(findWallet, order, tradeVolume);
        }
    }
}
