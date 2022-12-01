package OneCoin.Server.swap.mapper;

import OneCoin.Server.swap.dto.SwapDto;
import OneCoin.Server.swap.entity.Swap;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SwapMapper {
    Swap swapPostToSwap(SwapDto.Post requestBody);
    SwapDto.Response swapToSwapResponseDto(Swap swap);
    List<SwapDto.Response> swapsToSwapResponses(List<Swap> swaps);
}
