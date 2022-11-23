package OneCoin.Server.upbit.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.List;

@Getter
@RedisHash(value = "orderbook")
public class OrderBook { // 호가
    @Id
    private String code; // 마켓 코드
    private String total_ask_size; // 호가 매도 총 잔량
    private String total_bid_size; // 호가 매수 총 잔량
    private List<Object> orderbook_units; // 호가 정보
}
