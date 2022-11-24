package OneCoin.Server.balance.mapper;

import OneCoin.Server.balance.dto.BalanceDto;
import OneCoin.Server.balance.entity.Balance;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-11-24T17:09:54+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
@Component
public class BalanceMapperImpl implements BalanceMapper {

    @Override
    public Balance balancePatchToBalance(BalanceDto.Patch requestBody) {
        if ( requestBody == null ) {
            return null;
        }

        Balance balance = new Balance();

        balance.setBalance( requestBody.getBalance() );

        return balance;
    }

    @Override
    public BalanceDto.Response balanceToBalanceResponse(Balance balance) {
        if ( balance == null ) {
            return null;
        }

        BalanceDto.Response response = new BalanceDto.Response();

        if ( balance.getBalance() != null ) {
            response.setBalance( balance.getBalance() );
        }

        return response;
    }

    @Override
    public List<BalanceDto.Response> balancesToBalanceResponses(List<Balance> balances) {
        if ( balances == null ) {
            return null;
        }

        List<BalanceDto.Response> list = new ArrayList<BalanceDto.Response>( balances.size() );
        for ( Balance balance : balances ) {
            list.add( balanceToBalanceResponse( balance ) );
        }

        return list;
    }
}
