package OneCoin.Server.coin.controller;

import OneCoin.Server.coin.entity.Coin;
import OneCoin.Server.coin.repository.CoinRepository;
import OneCoin.Server.order.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final WalletRepository walletRepository;
    private final CoinRepository coinRepository;

    @PostMapping("/coin")
    public void postCoin(@RequestBody Coin coin) {
        coinRepository.save(coin);
    }

    @DeleteMapping("/wallet")
    public void deleteWallet() {
        walletRepository.deleteAll();
    } // 테스트용, 배포 시 제거
}
