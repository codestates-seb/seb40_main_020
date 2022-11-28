package OneCoin.Server.upbit.mapper;

import OneCoin.Server.upbit.dto.orderbook.AskInfo;
import OneCoin.Server.upbit.dto.orderbook.BidInfo;
import OneCoin.Server.upbit.dto.orderbook.OrderBookDto;
import OneCoin.Server.upbit.entity.UnitInfo;
import OneCoin.Server.utils.CalculationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderBookDtoMapper {
    private final CalculationUtil calculationUtil;

    public OrderBookDto unitInfoToOrderBookDto(OrderBookDto orderBookDto, List<UnitInfo> unitInfos, String prevClosingPrice) {
        List<AskInfo> askInfos = new ArrayList<>();
        List<BidInfo> bidInfos = new ArrayList<>();

        for (UnitInfo unitInfo : unitInfos) {
            String askPrice = unitInfo.getAskPrice();
            String askSize = unitInfo.getAskSize();
            String bidPrice = unitInfo.getBidPrice();
            String bidSize = unitInfo.getBidSize();

            AskInfo askInfo = new AskInfo(askPrice, askSize, calculationUtil.calculateChangeRate(askPrice, prevClosingPrice));
            BidInfo bidInfo = new BidInfo(bidPrice, bidSize, calculationUtil.calculateChangeRate(bidPrice, prevClosingPrice));
            askInfos.add(askInfo);
            bidInfos.add(bidInfo);

        }
        orderBookDto.setAskInfo(askInfos);
        orderBookDto.setBidInfo(bidInfos);
        return orderBookDto;
    }
}
