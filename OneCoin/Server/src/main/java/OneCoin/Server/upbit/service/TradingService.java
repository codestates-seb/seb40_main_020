package OneCoin.Server.upbit.service;

import OneCoin.Server.order.entity.Order;
import OneCoin.Server.order.service.OrderService;
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

    private final OrderService orderService;
    private final WalletService walletService;

    public void completeOrders(Trade trade) {
        List<Order> orders = orderService.findOrdersMatchingPrice(trade.getTradePrice(), trade.getOrderType(), trade.getCode());
        subtractAmount(orders, trade.getTradeVolume());
        orderService.updateAmountAfterTrade(orders);
    }

    private void subtractAmount(List<Order> orders, String amount) {
        BigDecimal subtractionAmount = new BigDecimal(amount);
        for (Order order : orders) {
            BigDecimal newAmount = order.getAmount().subtract(subtractionAmount);
            order.setAmount(newAmount);
        }
    }
}
