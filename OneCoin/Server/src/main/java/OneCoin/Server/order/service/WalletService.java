package OneCoin.Server.order.service;

import OneCoin.Server.order.entity.Wallet;
import OneCoin.Server.order.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    public Wallet updateWallet() {

//        return walletRepository.save();
        return null;
    }

    // ws
    public List<Wallet> findWallets(String code) {
        // User
        return null;
    }
}

// 매수, 매도에 따라 로직이 다름
// 매수 시, 있던 코인 업데이트
// 매도 시, 있던 코인 삭제