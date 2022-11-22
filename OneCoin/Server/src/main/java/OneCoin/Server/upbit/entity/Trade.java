package OneCoin.Server.upbit.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Trade { // 체결
    private String code; // 마켓 코드
    private String tradePrice; // 체결 가격 (원래 더블)
    private String tradeVolume; // 체결량 (원래 더블)
    private String orderType; // 매수/매도 구분 ASK(매도) BID(매수)
    private String tadeDate; // 체결 일자
    private String tradeTime; // 체결 시각 UTC기준, +9시간해야 한국 시간
}