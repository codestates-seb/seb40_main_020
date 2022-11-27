package OneCoin.Server.utils;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class CalculationUtil {

    public BigDecimal calculateAvgPriceByBid(BigDecimal holdingPrice, BigDecimal holdingAmount, BigDecimal newPrice, BigDecimal newAmount) {
        BigDecimal prevTotal = holdingPrice.multiply(holdingAmount);
        BigDecimal curTotal = newPrice.multiply(newAmount);
        BigDecimal totalAmount = holdingAmount.add(newAmount);

        return prevTotal.add(curTotal).divide(totalAmount, 2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateAvgPriceByAsk(BigDecimal holdingPrice, BigDecimal holdingAmount, BigDecimal newPrice, BigDecimal newAmount) {
        BigDecimal prevTotal = holdingPrice.multiply(holdingAmount);
        BigDecimal curTotal = newPrice.multiply(newAmount);
        BigDecimal totalAmount = holdingAmount.subtract(newAmount);

        return prevTotal.subtract(curTotal).divide(totalAmount, 2, RoundingMode.HALF_UP);
    }
}
