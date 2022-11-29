package OneCoin.Server.upbit.mapper;

import OneCoin.Server.upbit.dto.orderbook.AskInfo;
import OneCoin.Server.upbit.dto.orderbook.BidInfo;
import OneCoin.Server.upbit.dto.orderbook.OrderBookDto;
import OneCoin.Server.upbit.entity.UnitInfo;
import OneCoin.Server.utils.CalculationUtil;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class OrderBookDtoMapper {
    @Autowired
    protected CalculationUtil calculationUtil;

    @Mapping(target = "askInfo", expression = "java(toAskInfos(unitInfos, prevClosingPrice))")
    @Mapping(target = "bidInfo", expression = "java(toBidInfos(unitInfos, prevClosingPrice))")
    public abstract OrderBookDto unitInfoToOrderBookDto(List<UnitInfo> unitInfos, String prevClosingPrice);

    protected abstract List<AskInfo> toAskInfos(List<UnitInfo> unitInfos, @Context String prevClosingPrice);

    protected abstract List<BidInfo> toBidInfos(List<UnitInfo> unitInfos, @Context String prevClosingPrice);

    @Mapping(target = "changeRate", expression = "java(calculationUtil.calculateChangeRate(unitInfo.getAskPrice(), prevClosingPrice))")
    protected abstract AskInfo unitInfoToAskInfo(UnitInfo unitInfo, @Context String prevClosingPrice);

    @Mapping(target = "changeRate", expression = "java(calculationUtil.calculateChangeRate(unitInfo.getBidPrice(), prevClosingPrice))")
    protected abstract BidInfo unitInfoToBidInfo(UnitInfo unitInfo, @Context String prevClosingPrice);
}
