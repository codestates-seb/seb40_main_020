package OneCoin.Server.upbit.dto.ticker;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TickerDto {
    private String highPrice; // 24시간 고가
    private String lowPrice; // 24시간 저가
    private String tradePrice; // 현재가
    private String change; // 전일대비 RISE(상승) EVEN(보합) FALL(하락)
    private String changePrice; // 전일대비 값
    private String changeRate; // 전일대비 등락율
    private String accTradeVolume24h; // 24시간 누적 거래량
    private String accTradePrice24h; // 24시간 누적 거래 대금
}
