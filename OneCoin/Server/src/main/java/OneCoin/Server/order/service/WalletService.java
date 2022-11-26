package OneCoin.Server.order.service;

import OneCoin.Server.exception.BusinessLogicException;
import OneCoin.Server.exception.ExceptionCode;
import OneCoin.Server.order.entity.Wallet;
import OneCoin.Server.order.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    public Wallet createWallet() {
        return null;
    }

    public Wallet updateWallet() {

//        return walletRepository.save();
        return null;
    }

    public Wallet findVerifiedWalletWithCoin(long userId, String code) {
        Wallet wallet = findMyWallet(userId, code);
        if (wallet == null) {
            throw new BusinessLogicException(ExceptionCode.HAVE_NO_COIN);
        }
        return wallet;
    }

    // ws
    public Wallet findMyWallet(long userId, String code) {
        return walletRepository.findByUserIdAndCode(userId, code);
    }
}

// 매수, 매도에 따라 로직이 다름
// 매수 시, 있던 코인 업데이트
// 매도 시, 있던 코인 삭제