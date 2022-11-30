package OneCoin.Server.upbit.service;

import OneCoin.Server.helper.StubData;
import OneCoin.Server.upbit.dto.orderbook.OrderBookDto;
import OneCoin.Server.upbit.dto.ticker.TickerDto;
import OneCoin.Server.upbit.entity.Trade;
import OneCoin.Server.upbit.repository.OrderBookRepository;
import OneCoin.Server.upbit.repository.TickerRepository;
import OneCoin.Server.utils.JsonUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RecordApplicationEvents
@MockBean(OkHttpClient.class) // webSocket 끄기
public class UpbitHandlingServiceTest {
    @Autowired
    private UpbitHandlingService upbitHandlingService;

    @Autowired
    private JsonUtil jsonUtil;

    @Autowired
    private ApplicationEvents events;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TickerRepository tickerRepository;

    @Autowired
    private OrderBookRepository orderBookRepository;

    @Test
    @DisplayName("Ticker 정보를 받으면 entity를 저장한다.")
    void saveTickerTest() {
        // given
        String jsonTicker = StubData.MockUpbitAPI.getJsonTicker();
        JsonNode jsonNode = jsonUtil.fromJson(jsonTicker, JsonNode.class);

        // when
        upbitHandlingService.parsing(jsonNode);

        // then
        List<TickerDto> tickers = List.of(objectMapper.convertValue(tickerRepository.findTickers(), TickerDto[].class));
        assertThat(tickers.get(0).getCode()).isEqualTo("KRW-BTC");
        assertThat(tickers.get(0).getTimeStamp()).isEqualTo("1669706182202");
    }

    @Test
    @DisplayName("OrderBook 정보를 받으면 entity를 저장한다.")
    void saveOrderBookTest() {
        // given
        String jsonOrderBook = StubData.MockUpbitAPI.getJsonOrderBook();
        JsonNode jsonNode = jsonUtil.fromJson(jsonOrderBook, JsonNode.class);
        JsonNode jsonNodeTicker = jsonUtil.fromJson(StubData.MockUpbitAPI.getJsonTicker(), JsonNode.class);

        // when
        upbitHandlingService.parsing(jsonNodeTicker); // prevClosingPrice 때문에 실행
        upbitHandlingService.parsing(jsonNode);

        // then
        List<OrderBookDto> tickers = List.of(objectMapper.convertValue(orderBookRepository.findOrderBooks(), OrderBookDto[].class));
        assertThat(tickers.get(0).getCode()).isEqualTo("KRW-BTC");
        assertThat(tickers.get(0).getTotalAskSize()).isEqualTo("5.365745690000001");
    }

    @Test
    @DisplayName("Trade 정보를 받으면 이벤트가 발행된다.")
    void publishEventTest() {
        // given
        String trade = StubData.MockUpbitAPI.getJsonTicker();
        JsonNode jsonNode = jsonUtil.fromJson(trade, JsonNode.class);

        // when
        upbitHandlingService.parsing(jsonNode);

        // then
        int count = (int) events.stream(Trade.class).count();
        assertThat(count).isEqualTo(1);
    }
}
