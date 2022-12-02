package OneCoin.Server.swap.mapper;

import OneCoin.Server.order.entity.TransactionHistory;
import OneCoin.Server.order.entity.Wallet;
import OneCoin.Server.order.entity.enums.TransactionType;
import OneCoin.Server.swap.dto.SwapDto;
import OneCoin.Server.swap.entity.ExchangeRate;
import OneCoin.Server.swap.entity.Swap;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SwapMapper {
    @Mapping(source = "givenCoin", target = "givenCoin.code")
    @Mapping(source = "takenCoin", target = "takenCoin.code")
    Swap swapPostToSwap(SwapDto.Post requestBody);

    @Mapping(source = "givenCoin.code", target = "givenCoin")
    @Mapping(source = "takenCoin.code", target = "takenCoin")
    SwapDto.Response swapToSwapResponseDto(Swap swap);
    SwapDto.ExchangeRate exchangeRateToSwapExchangeRate(ExchangeRate exchangeRate);
    List<SwapDto.Response> swapsToSwapResponses(List<Swap> swaps);

    default Wallet swapToGivenWallet(Swap swap) {
        Wallet wallet = new Wallet();

        return wallet.builder()
                .userId(swap.getUser().getUserId())
                .code(swap.getGivenCoin().getCode())
                .amount(swap.getGivenAmount())
                .averagePrice(swap.getGivenCoinPrice())
                .build();
    }

    default Wallet swapToTakenWallet(Swap swap) {
        Wallet wallet = new Wallet();

        return wallet.builder()
                .userId(swap.getUser().getUserId())
                .code(swap.getTakenCoin().getCode())
                .amount(swap.getTakenAmount())
                .averagePrice(swap.getTakenCoinPrice())
                .build();
    }

    default TransactionHistory swapToTransactionHistory(Swap swap) {
        TransactionHistory transactionHistory = new TransactionHistory();

        return transactionHistory.builder()
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
}
