package OneCoin.Server.deposit.service;

import OneCoin.Server.balance.entity.Balance;
import OneCoin.Server.balance.service.BalanceService;
import OneCoin.Server.deposit.entity.Deposit;
import OneCoin.Server.deposit.repository.DepositRepository;
import OneCoin.Server.order.service.TransactionHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DepositService {
    private final DepositRepository depositRepository;
    private final BalanceService balanceService;
    private final TransactionHistoryService transactionHistoryService;

    /**
     * <pre>
     *     입금
     * </pre>
     */
    @Transactional
    public Deposit createDeposit(Deposit deposit) {
        deposit.setBalance(balanceService.updateBalance(deposit));
        deposit.setRemainingBalance(deposit.getBalance().getBalance());
        transactionHistoryService.createTransactionHistoryByDeposit(deposit);
        return depositRepository.save(deposit);
    }

    /**
     * <pre>
     *     balanceId 로 입금 목록 조회
     * </pre>
     */
    public Page<Deposit> findDepositsByBalanceId(Balance balance, int page, int size) {
        List<Deposit> depositList = depositRepository.findByBalance_BalanceId(balance.getBalanceId());

        Pageable pageable = PageRequest.of(page, size);
        PagedListHolder pagedListHolder = new PagedListHolder(depositList);
        pagedListHolder.setPageSize(size);
        pagedListHolder.setPage(page);

        return new PageImpl<>(pagedListHolder.getPageList(), pageable, depositList.size());
    }
}
