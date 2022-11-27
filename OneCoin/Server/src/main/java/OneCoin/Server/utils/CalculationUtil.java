package OneCoin.Server.utils;

import OneCoin.Server.order.entity.enums.Commission;
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

    public String calculateChangeRate(String price, String prevClosingPrice) {
        BigDecimal curPrice = new BigDecimal(price);
        BigDecimal prePrice = new BigDecimal(prevClosingPrice);
        BigDecimal changeRate = curPrice
                .subtract(prePrice)
                .multiply(new BigDecimal(100))
                .divide(prePrice, 2, RoundingMode.HALF_UP);
        return getSign(changeRate) + changeRate + "%";
    }

    private String getSign(BigDecimal changeRate) {
        int comparison = changeRate.compareTo(BigDecimal.ZERO);
        if (comparison > 0) {
            return "+";
        }
        return "";
    }

    public BigDecimal calculateBySubtractingCommission(BigDecimal price, BigDecimal amount) {
        BigDecimal commissionRate = BigDecimal.ONE.subtract(Commission.ORDER.getRate());
        return price.multiply(amount).multiply(commissionRate);
    }
}
