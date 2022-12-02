package OneCoin.Server.swap.mapper;

import OneCoin.Server.order.entity.TransactionHistory;
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

    default TransactionHistory swapToTransactionHistory(Swap swap) {
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
}
