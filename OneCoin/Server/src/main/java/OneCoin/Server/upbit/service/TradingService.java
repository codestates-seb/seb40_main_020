package OneCoin.Server.upbit.service;

import OneCoin.Server.order.entity.Order;
import OneCoin.Server.order.repository.OrderRepository;
import OneCoin.Server.order.service.WalletService;
import OneCoin.Server.upbit.entity.Trade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TradingService {

    private final OrderRepository orderRepository;
    private final WalletService walletService; // TODO 매도 채결시 wallet으로

    public void completeOrders(Trade trade) {
        String tradePrice = trade.getTradePrice();
        List<Order> orders = orderRepository.findAllByLimitAndOrderTypeAndCode(new BigDecimal(tradePrice), trade.getOrderType(), trade.getCode());
        if (orders.isEmpty()) {
            return;
        }
        subtractAmount(orders, trade.getTradeVolume());
        updateAmountAfterTrade(orders);
    }

    private void subtractAmount(List<Order> orders, String amount) {
        BigDecimal subtractionAmount = new BigDecimal(amount);
        for (Order order : orders) {
            BigDecimal newAmount = order.getAmount().subtract(subtractionAmount);
            order.setAmount(newAmount);
        }
    }

    private void updateAmountAfterTrade(List<Order> orders) {
        for (Order order : orders) {
            deleteAmountZeroEntity(order);
        }
        orderRepository.saveAll(orders);
    }

    private void deleteAmountZeroEntity(Order order) {
        BigDecimal zero = BigDecimal.ZERO;
        if (order.getAmount().compareTo(zero) <= 0) {
            orderRepository.delete(order);
        }
    }
}
