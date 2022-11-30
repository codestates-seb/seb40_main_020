package OneCoin.Server.deposit.mapper;

import OneCoin.Server.deposit.dto.DepositDto;
import OneCoin.Server.deposit.entity.Deposit;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DepositMapper {
    Deposit depositPostToDeposit(DepositDto.Post requestBody);

    default DepositDto.Response depositToDepositResponse(Deposit deposit) {
        DepositDto.Response response = new DepositDto.Response();

        response.setDepositAmount(deposit.getDepositAmount());
        response.setRemainingBalance(deposit.getRemainingBalance());
        response.setCreatedAt(deposit.getCreatedAt().toString());

        return response;
    }

    List<DepositDto.Response> depositsToDepositResponses(List<Deposit> deposits);
}
