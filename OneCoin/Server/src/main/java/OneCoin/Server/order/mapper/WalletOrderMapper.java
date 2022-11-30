package OneCoin.Server.order.mapper;

import OneCoin.Server.order.entity.Order;
import OneCoin.Server.order.entity.Wallet;
import OneCoin.Server.utils.CalculationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class WalletOrderMapper {
    public final CalculationUtil calculationUtil;

    public Wallet bidOrderToNewWallet(Order order, BigDecimal completedAmount) {
        BigDecimal averagePrice = calculationUtil.calculateAvgPrice(BigDecimal.ZERO, BigDecimal.ZERO, order.getLimit(), completedAmount);

        return Wallet.builder()
                .amount(completedAmount)
                .averagePrice(averagePrice)
                .userId(order.getUserId())
                .code(order.getCode())
                .build();
    }

    public Wallet bidOrderToUpdatedWallet(Wallet wallet, BigDecimal orderPrice, BigDecimal completedAmount) {
        BigDecimal averagePrice = calculationUtil.calculateAvgPrice(wallet.getAveragePrice(), wallet.getAmount(), orderPrice, completedAmount);
        BigDecimal totalAmount = wallet.getAmount().add(completedAmount);

        wallet.setAveragePrice(averagePrice);
        wallet.setAmount(totalAmount);
        return wallet;
    }

    public Wallet askOrderToUpdatedWallet(Wallet wallet, BigDecimal completedAmount) {
        BigDecimal totalAmount = wallet.getAmount().subtract(completedAmount);
        wallet.setAmount(totalAmount);
        return wallet;
    }
}
