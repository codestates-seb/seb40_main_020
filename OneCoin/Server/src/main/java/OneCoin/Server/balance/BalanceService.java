package OneCoin.Server.balance;

import OneCoin.Server.balance.entity.Balance;
import OneCoin.Server.balance.repository.BalanceRepository;
import OneCoin.Server.deposit.entity.Deposit;
import OneCoin.Server.exception.BusinessLogicException;
import OneCoin.Server.exception.ExceptionCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Positive;
import java.util.Optional;

@Service
@Transactional
public class BalanceService {
    private final BalanceRepository balanceRepository;

    public BalanceService(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    /**
     *  입금으로 Balance 변경
     */
    public Balance updateBalance(Deposit deposit) {
        Balance findBalance = findBalance(deposit.getBalance().getBalanceId());

        findBalance.setBalance(findBalance.getBalance() + deposit.getDepositAmount());

        return balanceRepository.save(findBalance);
    }
    
    /**
     *  userId로 balance 찾기
     */
    public Balance findBalanceByUserId(long userId) {
        Optional<Balance> optionalBalance = balanceRepository.findByUser_UserId(userId);
        Balance balance = optionalBalance.orElseThrow(() -> new BusinessLogicException(ExceptionCode.BALANCE_NOT_FOUND));
        return balance;
    }

    /**
     *  balanceId로 balance 찾기
     */
    public Balance findBalance(long balanceId) {
        Optional<Balance> optionalBalance = balanceRepository.findById(balanceId);
        Balance balance = optionalBalance.orElseThrow(() -> new BusinessLogicException(ExceptionCode.BALANCE_NOT_FOUND));
        return balance;
    }

    /**
     *  매수(BID) 체결 출금
     */
    public Balance updateBalanceByBid(long userId, @Positive long price) {
        Balance balance = findBalanceByUserId(userId);

        if (balance.getBalance() < price) {
            throw new BusinessLogicException(ExceptionCode.NOT_ENOUGH_BALANCE);
        }
        balance.setBalance(balance.getBalance() - price);

        return balanceRepository.save(balance);
    }

    /**
     *  매도(ASK) 체결 입금
     */
    public Balance updateBalanceByAsk(long userId, @Positive long price) {
        Balance balance = findBalanceByUserId(userId);

        balance.setBalance(balance.getBalance() + price);

        return balanceRepository.save(balance);
    }
}
