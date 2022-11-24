package OneCoin.Server.deposit.mapper;

import OneCoin.Server.deposit.dto.DepositDto;
import OneCoin.Server.deposit.entity.Deposit;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-11-24T23:13:50+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
@Component
public class DepositMapperImpl implements DepositMapper {

    @Override
    public Deposit depositPostToDeposit(DepositDto.Post requestBody) {
        if ( requestBody == null ) {
            return null;
        }

        Deposit deposit = new Deposit();

        deposit.setDepositAmount( requestBody.getDepositAmount() );

        return deposit;
    }

    @Override
    public List<DepositDto.Response> depositsToDepositResponses(List<Deposit> deposits) {
        if ( deposits == null ) {
            return null;
        }

        List<DepositDto.Response> list = new ArrayList<DepositDto.Response>( deposits.size() );
        for ( Deposit deposit : deposits ) {
            list.add( depositToDepositResponse( deposit ) );
        }

        return list;
    }
}
