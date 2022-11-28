package OneCoin.Server.upbit.dto.ticker;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class TickerDto {

    private String code;

    @JsonProperty("high_price")
    private String highPrice; // 24시간 고가

    @JsonProperty("low_price")
    private String lowPrice; // 24시간 저가

    @JsonProperty("trade_price")
    private String tradePrice; // 현재가

    @JsonProperty("prev_closing_price")
    private String prevClosingPrice; // 전일 종가

    private String change; // 전일대비 RISE(상승) EVEN(보합) FALL(하락)

    @JsonProperty("change_price")
    private String changePrice; // 전일대비 값

    @JsonProperty("change_rate")
    private String changeRate; // 전일대비 등락율

    @JsonProperty("acc_trade_volume_24h")
    private String accTradeVolume24h; // 24시간 누적 거래량

    @JsonProperty("acc_trade_price_24h")
    private String accTradePrice24h; // 24시간 누적 거래 대금

    @JsonProperty("timestamp")
    private String timeStamp;
}
