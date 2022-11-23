package OneCoin.Server.upbit.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "ticker")
public class Ticker { // 현재가
    @Id
    private String code; // 마켓 코드
    private String high_price; // 24시간 고가
    private String low_price; // 24시간 저가
    private String trade_price; // 현재가
    private String prev_closing_price; // 전일 종가
    private String change; // 전일대비 RISE(상승) EVEN(보합) FALL(하락)
    private String change_price; // 전일대비 값
    private String change_rate; // 전일대비 등락율
    private String acc_trade_volume_24h; // 24시간 누적 거래량
    private String acc_trade_price_24h; // 24시간 누적 거래 대금
}