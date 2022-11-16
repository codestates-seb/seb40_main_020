package OneCoin.Server.upbit.dto.ticker;

public class TickerDto {
    private double highPrice; // 24시간 고가
    private double lowPrice; // 24시간 저가
    private double tradePrice; // 현재가
    private String change; // 전일대비 RISE(상승) EVEN(보합) FALL(하락)
    private double changePrice; // 전일대비 값
    private double changeRate; // 전일대비 등락율
    private double acc_trade_volume_24h; // 24시간 누적 거래량
    private double acc_trade_price; // 24시간 누적 거래 대금
}
