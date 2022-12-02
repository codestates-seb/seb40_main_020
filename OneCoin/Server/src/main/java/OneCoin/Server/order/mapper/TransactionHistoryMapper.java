package OneCoin.Server.order.mapper;

import OneCoin.Server.coin.entity.Coin;
import OneCoin.Server.order.dto.TransactionHistoryDto;
import OneCoin.Server.order.entity.Order;
import OneCoin.Server.order.entity.TransactionHistory;
import OneCoin.Server.order.entity.enums.TransactionType;
import OneCoin.Server.swap.entity.Swap;
import OneCoin.Server.user.entity.User;
import OneCoin.Server.utils.CalculationUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class TransactionHistoryMapper {
    @Autowired
    protected CalculationUtil calculationUtil;

    public abstract List<TransactionHistoryDto.GetResponse> transactionHistoryToGetResponse(List<TransactionHistory> transactionHistories);

    @Mapping(target = "completedTime", source = "createdAt")
    @Mapping(target = "orderType", source = "transactionType.type")
    @Mapping(target = "code", source = "coin.code")
    public abstract TransactionHistoryDto.GetResponse entityToDto(TransactionHistory transactionHistory);

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

    public TransactionHistory swapToTransactionHistory(Swap swap) {
        return TransactionHistory.builder()
                .user(swap.getUser())
                .coin(swap.getGivenCoin())
                .transactionType(TransactionType.SWAP)
                .amount(swap.getGivenAmount())
                .price(swap.getGivenCoinPrice())
                .totalAmount(swap.getGivenAmount().multiply(swap.getGivenCoinPrice()))
                .commission(Double.parseDouble(swap.getCommission().toString()))
                .settledAmount(swap.getGivenAmount().subtract(swap.getCommission()))
                .orderTime(LocalDateTime.now())
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
