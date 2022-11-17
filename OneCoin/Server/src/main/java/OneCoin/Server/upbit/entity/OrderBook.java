package OneCoin.Server.upbit.entity;

import java.util.Map;

public class OrderBook { // 호가
    private String code; // 마켓 코드
    private double totalAskSize; // 호가 매도 총 잔량 - 옵션
    private double totalBidSize; // 호가 매수 총 잔량 - 옵션
    private Map<Double, Double> askPriceSize; // 매도 호가, 잔량
    private Map<Double, Double> bidPriceSize; // 매도 호가, 잔량
}
