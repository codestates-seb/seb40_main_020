package OneCoin.Server.order.mapper;

import OneCoin.Server.order.entity.Order;
import OneCoin.Server.order.entity.Wallet;
import OneCoin.Server.utils.CalculationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class WalletMapper {
    public final CalculationUtil calculationUtil;

    public Wallet bidOrderToWallet(Order order, BigDecimal completedAmount) {
        BigDecimal averagePrice = calculationUtil.calculateAvgPriceByBid(BigDecimal.ZERO, BigDecimal.ZERO, order.getLimit(), completedAmount);

        return Wallet.builder()
                .amount(completedAmount)
                .averagePrice(averagePrice)
                .userId(order.getUserId())
                .code(order.getCode())
                .build();
    }
}
