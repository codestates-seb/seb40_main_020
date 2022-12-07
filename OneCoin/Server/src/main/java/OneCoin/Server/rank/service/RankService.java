package OneCoin.Server.rank.service;

import OneCoin.Server.order.entity.Order;
import OneCoin.Server.order.entity.Wallet;
import OneCoin.Server.order.repository.OrderRepository;
import OneCoin.Server.order.repository.TransactionHistoryRepository;
import OneCoin.Server.order.repository.WalletRepository;
import OneCoin.Server.rank.dao.UserRoi;
import OneCoin.Server.rank.entity.Rank;
import OneCoin.Server.rank.repository.RankRepository;
import OneCoin.Server.upbit.dto.ticker.TickerDto;
import OneCoin.Server.upbit.repository.TickerRepository;
import OneCoin.Server.user.entity.User;
import OneCoin.Server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final UserRepository userRepository;


    /** 수익률 = (판가격 - 산가격) / 산가격
     * 판가격 = 실제로 판 거 + 아직 안 판 거의 현재 가격
     * 산가격 = 실제로 산 거 + 거래내역에는 없지만 월렛에는 있는 거( = Order를 보면 알 수 있다.)
     *
     * 실제 산 거 : transaction history bid 총합 + (Order의 부분체결된 레코드의 => 체결수량 * 매수가)
     * 실제 판 거 : transaction history ask 총합
     * 아직 안 판 거 : wallet * 수량 * 코인 현재가의 총합
     *
     * @return
     */



    public List<UserRoi> calculateAllRois() {
        Map<Long, UserRoi> allRoi = new HashMap<>();
        //유저별 매수가격 총액 O(n)
        setUserBids(allRoi);
        //유저별 부분체결의 체결 총액
        setPartialBids(allRoi);
        //유저별 매도가격 총액 O(n)
        setUserAsks(allRoi);
        // 유저별 현재 코인 가격 총액 O(n)
        setCurrentCoinValues(allRoi);
        // 계산 : ROI  = {(매도가격 총액 + 현재 가격 총액) - 매수가격 총액 } / 매수가격 총액 O(n)
        return calculateAll(allRoi);
    }

    private void setPartialBids(Map<Long, UserRoi> allRoi) {
        List<Order> orders = (List<Order>) orderRepository.findAll();
        for(Order order : orders) {
            log.info("{orderId : }", order.getOrderId());

            Long userId = order.getUserId();
            double bid = order.getCompletedAmount().multiply(order.getLimit()).doubleValue();
            log.info("userbid: {}" , bid);
            UserRoi userRoi = allRoi.get(userId);
            if(userRoi == null) {
                User user = userRepository.findById(userId).get();
                UserRoi userRoiCreated = new UserRoi();
                userRoiCreated.setUserId(userId);
                userRoiCreated.setUserDisplayName(user.getDisplayName());
                userRoi = allRoi.put(userId, userRoiCreated);
            }
            log.info("====rank: userRoi : {}", userRoi);
            log.info("====rank: bid : {}", bid);
            userRoi.addSumOfBids(bid);
        }
    }

    public List<UserRoi> calculateTop10() {
        List<UserRoi> allRoi = calculateAllRois();
        if(allRoi == null) return null;
        return allRoi.stream().sorted().limit(10).collect(Collectors.toList());
    }

    public void update(List<Rank> top10) {
        rankRepository.deleteAll();
        rankRepository.saveAll(top10);
    }

    public List<Rank> getTop10() {
        return rankRepository.findAll();
    }

    private void setUserBids(Map<Long, UserRoi> allRoi) {
        List<UserRoi> bids = transactionHistoryRepository.findAllSumOfBidSettledAmount();
        log.info("======Bids Size : " + bids.size());
        if(bids.size() == 0) return;
        for (UserRoi roi : bids) {
            allRoi.put(roi.getUserId(), roi);
        }
    }

    private void setUserAsks(Map<Long, UserRoi> allRoi) {
        List<UserRoi> asks = transactionHistoryRepository.findAllSumOfAskSettledAmount();
        log.info("======asks Size : " + asks.size());
        if(asks.size() == 0) return;
        for (UserRoi roi : asks) {
            Long userId = roi.getUserId();
            allRoi.get(userId).setSumOfAsks(roi.getSumOfAsks());
        }
    }

    private void setCurrentCoinValues(Map<Long, UserRoi> allRoi) {
        List<Wallet> wallets = (List<Wallet>) walletRepository.findAll();
        log.info("======wallet Size : " + wallets.size());
        if(wallets.size() == 0) return;
        for (Wallet wallet : wallets) {
            Long userId = wallet.getUserId();
            UserRoi userROI = allRoi.get(userId);
            if(userROI == null) continue;
            TickerDto ticker = tickerRepository.findTickerByCode(wallet.getCode());
            if(userId == 90L) {
                log.info("amount = " + wallet.getAmount().doubleValue());
                log.info("value = " + Double.valueOf(ticker.getTradePrice()));
            }
            double currentValueOfCoinOfUser = wallet.getAmount().doubleValue() * Double.valueOf(ticker.getTradePrice());
            userROI.addSumOfCurrentCoinValues(currentValueOfCoinOfUser);
        }
    }

    private List<UserRoi> calculateAll(Map<Long, UserRoi> allRoi) {
        if(allRoi.size() == 0) return null;
        List<UserRoi> rois = new ArrayList<>(allRoi.values());
        for (UserRoi userROI : rois) {
            userROI.calculate();
        }
        return rois;
    }
}
