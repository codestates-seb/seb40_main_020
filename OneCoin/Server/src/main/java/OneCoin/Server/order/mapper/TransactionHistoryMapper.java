package OneCoin.Server.order.mapper;

import OneCoin.Server.order.dto.TransactionHistoryDto;
import OneCoin.Server.order.entity.TransactionHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionHistoryMapper {

    List<TransactionHistoryDto.GetResponse> transactionHistoryToGetResponse(List<TransactionHistory> transactionHistories);

    @Mapping(target = "completedTime", source = "createdAt")
    @Mapping(target = "orderType", source = "transactionType.type")
    @Mapping(target = "code", source = "coin.code")
    TransactionHistoryDto.GetResponse entityToDto(TransactionHistory transactionHistory);
}
