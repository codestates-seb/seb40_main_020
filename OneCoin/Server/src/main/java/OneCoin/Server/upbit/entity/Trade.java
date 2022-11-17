package OneCoin.Server.upbit.entity;

public class Trade { // 체결
    private String code; // 마켓 코드
    private double tradePrice; // 체결 가격
    private double tradeVolume; // 체결량
    private String askBid; // 매수/매도 구분 ASK(매도) BID(매수)
    private String tadeDate; // 체결 일자
    private String tradeTime; // 체결 시각
}