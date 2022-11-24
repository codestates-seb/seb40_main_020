package OneCoin.Server.deposit.service;

import OneCoin.Server.balance.BalanceService;
import OneCoin.Server.deposit.entity.Deposit;
import OneCoin.Server.deposit.repository.DepositRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class DepositService {
    private final DepositRepository depositRepository;
    private final BalanceService balanceService;

    public DepositService(DepositRepository depositRepository, BalanceService balanceService) {
        this.depositRepository = depositRepository;
        this.balanceService = balanceService;
    }

    /**
     * 입금
     */
    @Transactional
    public Deposit createDeposit(Deposit deposit) {
        deposit.setBalance(balanceService.updateBalance(deposit));
        return depositRepository.save(deposit);
    }
}
