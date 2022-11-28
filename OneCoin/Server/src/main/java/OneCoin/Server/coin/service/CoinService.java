package OneCoin.Server.coin.service;

import OneCoin.Server.coin.entity.Coin;
import OneCoin.Server.coin.repository.CoinRepository;
import OneCoin.Server.exception.BusinessLogicException;
import OneCoin.Server.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CoinService {

    private final CoinRepository coinRepository;

    public void verifyCoinExists(String code) {
        Optional<Coin> optionalCoin = coinRepository.findByCode(code);
        if (optionalCoin.isEmpty()) {
            throw new BusinessLogicException(ExceptionCode.COIN_NOT_EXISTS);
        }
    }

    public Coin findCoin(String code) {
        Optional<Coin> optionalCoin = coinRepository.findByCode(code);
        return optionalCoin.orElseThrow(() -> new BusinessLogicException(ExceptionCode.COIN_NOT_EXISTS));
    }

    // TODO 관리자가 코인 등록
}
