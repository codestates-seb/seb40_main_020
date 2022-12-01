package OneCoin.Server.swap.mapper;

import OneCoin.Server.swap.dto.SwapDto;
import OneCoin.Server.swap.entity.Swap;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SwapMapper {
    @Mapping(source = "givenCoin", target = "givenCoin.code")
    @Mapping(source = "takenCoin", target = "takenCoin.code")
    Swap swapPostToSwap(SwapDto.Post requestBody);

    @Mapping(source = "givenCoin.code", target = "givenCoin")
    @Mapping(source = "takenCoin.code", target = "takenCoin")
    SwapDto.Response swapToSwapResponseDto(Swap swap);
    List<SwapDto.Response> swapsToSwapResponses(List<Swap> swaps);
}
