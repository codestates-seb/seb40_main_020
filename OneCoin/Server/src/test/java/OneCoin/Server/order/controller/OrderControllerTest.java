package OneCoin.Server.order.controller;


import OneCoin.Server.helper.StubData;
import OneCoin.Server.order.dto.RedisOrderDto;
import OneCoin.Server.order.entity.RedisOrder;
import OneCoin.Server.order.service.OrderService;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private Gson gson;

    @SneakyThrows
    @Test
    @DisplayName("slice test: 매수/매도 주문")
    void postOrderTest() {
        // given
        RedisOrderDto.Post redisPostDto = StubData.MockRedisPostDto.getMockRedisPost();
        String content = gson.toJson(redisPostDto);

        RedisOrder redisOrder = StubData.MockRedisOrder.getMockEntity();
        given(orderService.createOrder(Mockito.any(RedisOrder.class), Mockito.anyString()))
                .willReturn(redisOrder);

        // when
        mockMvc.perform(
                        post("/api/order/KRW-BTC")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.orderId").value(redisOrder.getOrderId()));
    }
}
