package OneCoin.Server.rank.service;

import OneCoin.Server.order.entity.Order;
import OneCoin.Server.order.entity.TransactionHistory;
import OneCoin.Server.order.entity.Wallet;
import OneCoin.Server.order.entity.enums.TransactionType;
import OneCoin.Server.order.repository.OrderRepository;
import OneCoin.Server.order.repository.TransactionHistoryRepository;
import OneCoin.Server.order.repository.WalletRepository;
import OneCoin.Server.rank.dao.UserRoi;
import OneCoin.Server.rank.entity.Rank;
import OneCoin.Server.rank.entity.RankHistory;
import OneCoin.Server.rank.repository.RankHistoryRepository;
import OneCoin.Server.rank.repository.RankRepository;
import OneCoin.Server.upbit.dto.ticker.TickerDto;
import OneCoin.Server.upbit.repository.TickerRepository;
import OneCoin.Server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RankService {
    private final TransactionHistoryRepository transactionHistoryRepository;
    private final TickerRepository tickerRepository;
    private final WalletRepository walletRepository;
    private final RankRepository rankRepository;
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final RankHistoryRepository rankHistoryRepository;


    /**
     * 수익률 = (판가격 - 산가격) / 산가격
     * 판가격 = 실제 매도 금액 + 잠재적 매도 금액(현재 보유 중인 코인의 현재 가치)(Wallet)
     * 산가격 = 매수 요청 중 전체 체결된 금액(RDB) + 매수 요청 중 부분 체결된 금액(Order)
     * <p>
     * 실제 매도 또는 매수가 체결될 때마다 누적합이 계산됨.
     * '잠재적 매도 금액'과, '매수 요청 중 부분 체결된 금액'만 계산하면 됨.
     */
    public List<UserRoi> calculateAllRois() {
        Map<Long, UserRoi> allRoi = new HashMap<>();
        setAccumulatedAsksAndBids(allRoi);
        setPartialBids(allRoi);
        setCurrentCoinValues(allRoi);
        setDisplayName(allRoi);
        return calculateAll(allRoi);
    }

    private void setDisplayName(Map<Long, UserRoi> allRoi) {
        for (Map.Entry<Long, UserRoi> entry : allRoi.entrySet()) {
            Long userId = entry.getKey();
            UserRoi roi = entry.getValue();
            String displayName = userService.findVerifiedUser(userId).getDisplayName();
            roi.setUserDisplayName(displayName);
        }
    }

    @Transactional
    public void accumulateTransaction(TransactionHistory transactionHistory) {
        TransactionType transactionType = transactionHistory.getTransactionType();
        Long userId = transactionHistory.getUser().getUserId();
        RankHistory rankHistory = findVerifiedRankHistory(userId);
        if (transactionType.equals(TransactionType.ASK)) {
            rankHistory.setAccumulatedAsk(rankHistory.getAccumulatedAsk().add(transactionHistory.getTotalAmount()));
        } else if (transactionType.equals(TransactionType.BID)) {
            rankHistory.setAccumulatedBid(rankHistory.getAccumulatedBid().add(transactionHistory.getTotalAmount()));
        }
        rankHistoryRepository.save(rankHistory);
    }

    @Transactional
    private RankHistory findVerifiedRankHistory(Long userId) {
        Optional<RankHistory> optionalRankHistory = rankHistoryRepository.findByUserId(userId);
        RankHistory rankHistory;
        if (optionalRankHistory.isEmpty()) {
            rankHistory = rankHistoryRepository.save(RankHistory.builder()
                    .userId(userId)
                    .accumulatedAsk(BigDecimal.ZERO)
                    .accumulatedBid(BigDecimal.ZERO)
                    .build());
        } else {
            rankHistory = optionalRankHistory.get();
        }
        return rankHistory;
    }


    private void setAccumulatedAsksAndBids(Map<Long, UserRoi> allRoi) {
        List<RankHistory> rankHistories = rankHistoryRepository.findAll();
        for (RankHistory rankHistory : rankHistories) {
            UserRoi userRoi = new UserRoi(rankHistory.getUserId(), rankHistory.getAccumulatedBid().longValue(), rankHistory.getAccumulatedAsk().longValue());
            allRoi.put(rankHistory.getUserId(), userRoi);
        }
    }

    private void setPartialBids(Map<Long, UserRoi> allRoi) {
        List<Order> orders = (List<Order>) orderRepository.findAll();
        for (Order order : orders) {
            Long userId = order.getUserId();
            double bid = order.getCompletedAmount().multiply(order.getLimit()).doubleValue();
            if (bid == 0.0) continue;
            UserRoi userRoi = allRoi.get(userId);
            if (userRoi == null) {
                UserRoi userRoiCreated = new UserRoi();
                userRoiCreated.setUserId(userId);
                allRoi.put(userId, userRoiCreated);
                userRoi = allRoi.get(userId);
            }
            userRoi.addSumOfBids(bid);
        }
    }

    public List<UserRoi> calculateTop10() {
        List<UserRoi> allRoi = calculateAllRois();
        if (allRoi == null) return null;
        return allRoi.stream().sorted().limit(10).collect(Collectors.toList());
    }

    public void update(List<Rank> top10) {
        rankRepository.deleteAll();
        rankRepository.saveAll(top10);
    }

    public List<Rank> getTop10() {
        return rankRepository.findAll();
    }


    private void setCurrentCoinValues(Map<Long, UserRoi> allRoi) {
        List<Wallet> wallets = (List<Wallet>) walletRepository.findAll();
        if (wallets.size() == 0) return;
        for (Wallet wallet : wallets) {
            Long userId = wallet.getUserId();
            UserRoi userROI = allRoi.get(userId);
            if (userROI == null) continue;
            TickerDto ticker = tickerRepository.findTickerByCode(wallet.getCode());
            double currentValueOfCoinOfUser = wallet.getAmount().doubleValue() * Double.valueOf(ticker.getTradePrice());
            userROI.addSumOfCurrentCoinValues(currentValueOfCoinOfUser);
        }
    }

    private List<UserRoi> calculateAll(Map<Long, UserRoi> allRoi) {
        if (allRoi.size() == 0) return null;
        List<UserRoi> rois = new ArrayList<>(allRoi.values());
        for (UserRoi userROI : rois) {
            userROI.calculate();
        }
        return rois;
    }
}
