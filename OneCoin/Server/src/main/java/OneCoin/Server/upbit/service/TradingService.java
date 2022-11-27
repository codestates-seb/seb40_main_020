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
    private final WalletService walletService;

    public void completeOrders(Trade trade) {
        BigDecimal tradePrice = new BigDecimal(trade.getTradePrice());
        BigDecimal tradeVolume = new BigDecimal(trade.getTradeVolume());
        String orderType = trade.getOrderType();

        List<Order> orders = orderRepository.findAllByLimitAndOrderTypeAndCode(tradePrice, orderType, trade.getCode());
        if (orders.isEmpty()) {
            return;
        }
    }
}
