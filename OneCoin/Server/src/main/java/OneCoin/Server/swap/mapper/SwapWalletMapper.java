package OneCoin.Server.swap.mapper;

import OneCoin.Server.order.entity.Wallet;
import OneCoin.Server.swap.entity.Swap;
import OneCoin.Server.utils.CalculationUtil;
import org.springframework.stereotype.Component;

@Component
public class SwapWalletMapper {
    public final CalculationUtil calculationUtil;

    public SwapWalletMapper(CalculationUtil calculationUtil) {
        this.calculationUtil = calculationUtil;
    }

    public Wallet swapToGivenWallet(Swap swap) {

        return Wallet.builder()
                .userId(swap.getUser().getUserId())
                .code(swap.getGivenCoin().getCode())
                .amount(swap.getGivenAmount())
                .averagePrice(swap.getGivenCoinPrice())
                .build();
    }

    public Wallet swapToTakenWallet(Swap swap) {

        return Wallet.builder()
                .userId(swap.getUser().getUserId())
                .code(swap.getTakenCoin().getCode())
                .amount(swap.getTakenAmount())
                .averagePrice(swap.getTakenCoinPrice())
                .build();
    }
}
