package OneCoin.Server.balance.service;

import OneCoin.Server.balance.entity.Balance;
import OneCoin.Server.balance.repository.BalanceRepository;
import OneCoin.Server.deposit.entity.Deposit;
import OneCoin.Server.exception.BusinessLogicException;
import OneCoin.Server.exception.ExceptionCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
public class BalanceService {
    private final BalanceRepository balanceRepository;

    public BalanceService(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    /**
     * <pre>
     *     입금으로 Balance 변경
     * </pre>
     */
    public Balance updateBalance(Deposit deposit) {
        Balance findBalance = findBalance(deposit.getBalance().getBalanceId());

        BigDecimal depositAmount = new BigDecimal(deposit.getDepositAmount());
        findBalance.setBalance(findBalance.getBalance().add(depositAmount));

        return balanceRepository.save(findBalance);
    }

    /**
     * <pre>
     *     userId로 balance 찾기
     * </pre>
     */
    public Balance findBalanceByUserId(long userId) {
        Optional<Balance> optionalBalance = balanceRepository.findByUser_UserId(userId);
        Balance balance = optionalBalance.orElseThrow(() -> new BusinessLogicException(ExceptionCode.BALANCE_NOT_FOUND));
        return balance;
    }

    /**
     * <pre>
     *     balanceId로 balance 찾기
     * </pre>
     */
    public Balance findBalance(long balanceId) {
        Optional<Balance> optionalBalance = balanceRepository.findById(balanceId);
        Balance balance = optionalBalance.orElseThrow(() -> new BusinessLogicException(ExceptionCode.BALANCE_NOT_FOUND));
        return balance;
    }

    /**
     * <pre>
     *     매수(BID) 체결 출금
     * </pre>
     */
    public void updateBalanceByBid(long userId, @Positive BigDecimal price) {
        Balance balance = findBalanceByUserId(userId);
        int comparison = balance.getBalance().compareTo(price);
        if (comparison < 0) {
            throw new BusinessLogicException(ExceptionCode.NOT_ENOUGH_BALANCE);
        }
        balance.setBalance(balance.getBalance().subtract(price));

        balanceRepository.save(balance);
    }

    /**
     * <pre>
     *     매도(ASK) 체결 입금, 미체결된 매수(BID) 취소 입금
     * </pre>
     */
    public void updateBalanceByAskOrCancelBid(long userId, @Positive BigDecimal price) {
        Balance balance = findBalanceByUserId(userId);

        balance.setBalance(balance.getBalance().add(price));

        balanceRepository.save(balance);
    }
}
