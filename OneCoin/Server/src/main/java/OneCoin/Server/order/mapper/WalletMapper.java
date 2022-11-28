package OneCoin.Server.order.mapper;

import OneCoin.Server.order.dto.WalletDto;
import OneCoin.Server.order.entity.Wallet;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WalletMapper {

    List<WalletDto.GetResponse> walletToGetResponse(List<Wallet> wallets);
}
