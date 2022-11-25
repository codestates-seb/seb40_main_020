package OneCoin.Server.balance.mapper;

import OneCoin.Server.balance.dto.BalanceDto;
import OneCoin.Server.balance.entity.Balance;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BalanceMapper {
    Balance balancePatchToBalance(BalanceDto.Patch requestBody);
    BalanceDto.Response balanceToBalanceResponse(Balance balance);
    List<BalanceDto.Response> balancesToBalanceResponses(List<Balance> balances);
}
