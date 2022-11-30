package OneCoin.Server.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class CalculationUtilTest {

    private final CalculationUtil calculationUtil = new CalculationUtil();

    @Test
    @DisplayName("매수 채결 시 평단가 계산")
    void calculateAveragePrice() {
        // given
        BigDecimal holdingPrice = new BigDecimal("1234.551");
        BigDecimal holdingAmount = new BigDecimal("0.234");
        BigDecimal newPrice = new BigDecimal("5902.6123");
        BigDecimal newAmount = new BigDecimal("2.15568");

        // when
        BigDecimal averagePrice = calculationUtil.calculateAvgPrice(holdingPrice, holdingAmount, newPrice, newAmount);

        // then
        assertThat(averagePrice).isEqualTo(new BigDecimal("5445.51"));
    }

    @Test
    @DisplayName("수익률 계산 (양수)")
    void calculateChangeRate1() {
        // given
        String curPrice = "22460000";
        String prevPrice = "22339000";

        // when
        String changeRate = calculationUtil.calculateChangeRate(curPrice, prevPrice);

        // then
        assertThat(changeRate).isEqualTo("+0.54%");
    }

    @Test
    @DisplayName("수익률 계산 (음수)")
    void calculateChangeRate2() {
        // given
        String curPrice = "22339000";
        String prevPrice = "22646000";

        // when
        String changeRate = calculationUtil.calculateChangeRate(curPrice, prevPrice);

        // then
        assertThat(changeRate).isEqualTo("-1.36%");
    }

    @Test
    @DisplayName("매도 시 수수료를 제외한 금액 계산")
    void calculateCommissionByAsk() {
        // given
        BigDecimal price = new BigDecimal("225612.666");
        BigDecimal amount = new BigDecimal("1.2366");

        // when
        BigDecimal commission = calculationUtil.calculateBySubtractingCommission(price, amount);

        // given
        assertThat(commission).isEqualTo(new BigDecimal("278853.12646421220"));
    }

    @Test
    @DisplayName("매수 시 수수료를 더한 금액 계산")
    void calculateCommissionByBid() {
        // given
        BigDecimal price = new BigDecimal("225612.666");
        BigDecimal amount = new BigDecimal("1.2366");

        // when
        BigDecimal commission = calculationUtil.calculateByAddingCommission(price, amount);

        // given
        assertThat(commission).isEqualTo(new BigDecimal("279132.11908698780"));
    }

    @Test
    @DisplayName("수수료 책정")
    void calculateCommission() {
        // given
        BigDecimal price = new BigDecimal("225612.666");
        BigDecimal amount = new BigDecimal("1.2366");

        // when
        BigDecimal commission = calculationUtil.calculateOrderCommission(price, amount);

        // given
        assertThat(commission).isEqualTo(new BigDecimal("139.49631138780"));
    }
}
