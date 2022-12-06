package OneCoin.Server.admin;

import OneCoin.Server.balance.repository.BalanceRepository;
import OneCoin.Server.chat.repository.ChatMessageRdbRepository;
import OneCoin.Server.chat.repository.ChatMessageRepository;
import OneCoin.Server.chat.repository.LastSavedRepository;
import OneCoin.Server.chat.repository.UserInChatRoomRepository;
import OneCoin.Server.coin.entity.Coin;
import OneCoin.Server.coin.repository.CoinRepository;
import OneCoin.Server.deposit.repository.DepositRepository;
import OneCoin.Server.order.repository.OrderRepository;
import OneCoin.Server.order.repository.TransactionHistoryRepository;
import OneCoin.Server.order.repository.WalletRepository;
import OneCoin.Server.rank.repository.RankRepository;
import OneCoin.Server.swap.repository.SwapRepository;
import OneCoin.Server.user.repository.AuthRepository;
import OneCoin.Server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final BalanceRepository balanceRepository;
    private final ChatMessageRdbRepository chatMessageRdbRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final LastSavedRepository lastSavedRepository;
    private final UserInChatRoomRepository userInChatRoomRepository;
    private final DepositRepository depositRepository;
    private final OrderRepository orderRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final WalletRepository walletRepository;
    private final RankRepository rankRepository;
    private final SwapRepository swapRepository;
    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    private final CoinRepository coinRepository;
    @DeleteMapping
    void deleteAll() {
        //잔액
        balanceRepository.deleteAll();
        //채팅
        chatMessageRdbRepository.deleteAll();
        chatMessageRepository.removeAllInChatRoom(1);
        chatMessageRepository.removeAllInChatRoom(2);
        lastSavedRepository.delete(1);
        lastSavedRepository.delete(2);
        userInChatRoomRepository.removeAllInChatRoom(1);
        userInChatRoomRepository.removeAllInChatRoom(2);
        //입금
        depositRepository.deleteAll();
        //주문
        orderRepository.deleteAll();
        transactionHistoryRepository.deleteAll();
        walletRepository.deleteAll();
        //랭크
        rankRepository.deleteAll();
        //스왑
        swapRepository.deleteAll();
        //유저
        authRepository.deleteAll();
        userRepository.deleteAll();
    }

    @PostMapping("/coin")
    public void postCoin(@RequestBody Coin coin) {
        coinRepository.save(coin);
    }
}
