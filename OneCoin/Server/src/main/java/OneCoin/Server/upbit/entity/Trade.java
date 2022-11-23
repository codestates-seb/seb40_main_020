package OneCoin.Server.upbit.entity;

import lombok.Getter;

@Getter
public class Trade { // 체결
    private String code; // 마켓 코드
    private String trade_price; // 체결 가격
    private String trade_volume; // 체결량
    private String ask_bid; // 매수/매도 구분 ASK(매도) BID(매수)
    private String trade_time; // 체결 시각 UTC기준, +9시간해야 한국 시간
}