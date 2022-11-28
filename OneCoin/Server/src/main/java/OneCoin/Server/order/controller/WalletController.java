package OneCoin.Server.order.controller;

import OneCoin.Server.dto.MultiResponseDto;
import OneCoin.Server.order.dto.WalletDto;
import OneCoin.Server.order.entity.Wallet;
import OneCoin.Server.order.mapper.WalletMapper;
import OneCoin.Server.order.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/order/my-coin")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;
    private final WalletMapper mapper;

    @GetMapping
    public ResponseEntity getMyWallet() {
        List<Wallet> wallets = walletService.findWallets();
        List<WalletDto.GetResponse> responseDto = mapper.walletToGetResponse(wallets);

        return new ResponseEntity(new MultiResponseDto<>(responseDto), HttpStatus.OK);
    }
}
