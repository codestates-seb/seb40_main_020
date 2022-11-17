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

    @Transactional(readOnly = true)
    public Coin findCoin(String code) {
        return findVerifiedCoin(code);
    }

    private Coin findVerifiedCoin(String code) {
        Optional<Coin> optionalCoin = coinRepository.findByCode(code);
        return optionalCoin.orElseThrow(() -> new BusinessLogicException(ExceptionCode.COIN_NOT_EXISTS));
    }
}
