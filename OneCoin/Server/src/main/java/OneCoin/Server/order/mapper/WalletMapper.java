package OneCoin.Server.order.mapper;

import OneCoin.Server.order.dto.WalletDto;
import OneCoin.Server.order.entity.Order;
import OneCoin.Server.order.entity.Wallet;
import OneCoin.Server.utils.CalculationUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class WalletMapper {
    @Autowired
    protected CalculationUtil calculationUtil;

    public abstract List<WalletDto.GetResponse> walletToGetResponse(List<Wallet> wallets);

    @Mapping(target = "amount", source = "completedAmount")
    @Mapping(target = "averagePrice", expression = "java(calculationUtil.calculateAvgPrice(BigDecimal.ZERO, BigDecimal.ZERO, order.getLimit(), completedAmount))")
    @Mapping(target = "userId", source = "order.userId")
    @Mapping(target = "code", source = "order.code")
    public abstract Wallet bidOrderToNewWallet(Order order, BigDecimal completedAmount);

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
