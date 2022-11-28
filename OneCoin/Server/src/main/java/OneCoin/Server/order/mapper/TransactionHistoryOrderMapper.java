package OneCoin.Server.order.mapper;

import OneCoin.Server.coin.entity.Coin;
import OneCoin.Server.order.entity.Order;
import OneCoin.Server.order.entity.TransactionHistory;
import OneCoin.Server.order.entity.enums.TransactionType;
import OneCoin.Server.user.entity.User;
import OneCoin.Server.utils.CalculationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@RequiredArgsConstructor
public class TransactionHistoryOrderMapper {
    private final CalculationUtil calculationUtil;

    public TransactionHistory orderToTransactionHistory(Order order, User user, Coin coin) {
        BigDecimal completedAmount = order.getCompletedAmount();
        BigDecimal price = order.getLimit();
        BigDecimal totalAmount = price.multiply(completedAmount);

        TransactionType transactionType = TransactionType.valueOf(order.getOrderType());
        BigDecimal commission = calculationUtil.calculateOrderCommission(price, completedAmount).setScale(2, RoundingMode.HALF_UP);
        BigDecimal settledAmount = getSettledAmount(transactionType, totalAmount, commission);


        return TransactionHistory.builder()
                .transactionType(transactionType)
                .amount(completedAmount)
                .price(price)
                .totalAmount(totalAmount)
                .commission(commission.doubleValue())
                .settledAmount(settledAmount)
                .orderTime(order.getOrderTime())
                .user(user)
                .coin(coin)
                .build();
    }

    private BigDecimal getSettledAmount(TransactionType transactionType, BigDecimal totalAmount, BigDecimal commission) {
        BigDecimal settledAmount = null;
        if (transactionType.equals(TransactionType.BID)) {
            settledAmount = totalAmount.add(commission);
        }
        if (transactionType.equals(TransactionType.ASK)) {
            settledAmount = totalAmount.subtract(commission);
        }
        return settledAmount;
    }
}