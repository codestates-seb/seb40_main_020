package OneCoin.Server.upbit.service;

import OneCoin.Server.helper.StubData;
import OneCoin.Server.order.entity.Order;
import OneCoin.Server.order.repository.OrderRepository;
import OneCoin.Server.order.service.WalletService;
import OneCoin.Server.upbit.entity.Trade;
import OneCoin.Server.utils.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@SpringBootTest
@MockBean(OkHttpClient.class) // webSocket 끄기
public class TradingServiceTest {
    @Autowired
    private JsonUtil jsonUtil;

    @MockBean
    private WalletService walletService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TradingService tradingService;

    @SpyBean
    private OrderRepository orderRepository;
    private Trade trade;

    @BeforeEach
    void createTradeEntity() throws JsonProcessingException {
        String jsonTicker = StubData.MockUpbitAPI.getJsonTicker();
        JsonNode jsonNode = jsonUtil.fromJson(jsonTicker, JsonNode.class);
        trade = objectMapper.readValue(jsonNode.toString(), Trade.class);

        Order order = StubData.MockOrder.getMockEntity();
        orderRepository.save(order);
    }

    @Test
    @DisplayName("OrderRepository의 메서드를 실행한다.")
    void invokeTest() {
        // given
        when(walletService.findMyWallet(anyLong(), anyString())).thenReturn(null);
        doNothing().when(walletService).createWallet(any(), any());

        // when
        tradingService.completeOrders(trade);

        // then
        verify(orderRepository, times(1)).findAllByLimitAndOrderTypeAndCode(new BigDecimal(trade.getTradePrice()), trade.getOrderType(), trade.getCode());
    }
}
