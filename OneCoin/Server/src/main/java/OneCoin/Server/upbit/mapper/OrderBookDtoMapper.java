package OneCoin.Server.upbit.mapper;

import OneCoin.Server.upbit.dto.orderbook.AskInfo;
import OneCoin.Server.upbit.dto.orderbook.BidInfo;
import OneCoin.Server.upbit.dto.orderbook.OrderBookDto;
import OneCoin.Server.upbit.entity.UnitInfo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderBookDtoMapper {

    public OrderBookDto unitInfoToOrderBookDto(OrderBookDto orderBookDto, List<UnitInfo> unitInfos, String prevClosingPrice) {
        List<AskInfo> askInfos = new ArrayList<>();
        List<BidInfo> bidInfos = new ArrayList<>();

        for (UnitInfo unitInfo : unitInfos) {
            String askPrice = unitInfo.getAskPrice();
            String askSize = unitInfo.getAskSize();
            String bidPrice = unitInfo.getBidPrice();
            String bidSize = unitInfo.getBidSize();

            AskInfo askInfo = new AskInfo(askPrice, askSize, calculateChangeRate(askPrice, prevClosingPrice));
            BidInfo bidInfo = new BidInfo(bidPrice, bidSize, calculateChangeRate(bidPrice, prevClosingPrice));
            askInfos.add(askInfo);
            bidInfos.add(bidInfo);

        }
        orderBookDto.setAskInfo(askInfos);
        orderBookDto.setBidInfo(bidInfos);
        return orderBookDto;
    }

    private String calculateChangeRate(String price, String prevClosingPrice) {
        BigDecimal curPrice = new BigDecimal(price);
        BigDecimal prePrice = new BigDecimal(prevClosingPrice);
        BigDecimal changeRate = curPrice
                .subtract(prePrice)
                .multiply(new BigDecimal(100))
                .divide(prePrice, 2, RoundingMode.HALF_UP);
        return getSign(changeRate) + changeRate + "%";
    }

    private String getSign(BigDecimal changeRate) {
        int comparison = changeRate.compareTo(BigDecimal.ZERO);
        if (comparison > 0) {
            return "+";
        }
        return "";
    }
}
