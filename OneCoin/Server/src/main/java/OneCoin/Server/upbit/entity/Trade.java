package OneCoin.Server.upbit.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Trade { // 체결

    private String code; // 마켓 코드

    @JsonProperty("trade_price")
    private String tradePrice; // 체결 가격

    @JsonProperty("trade_volume")
    private String tradeVolume; // 체결량

    @JsonProperty("ask_bid")
    private String orderType; // 매수,매도 구분 ASK(매도) BID(매수)

    @JsonProperty("trade_time")
    private String tradeTime; // 체결 시각 UTC기준
}