package OneCoin.Server.order.controller;

import OneCoin.Server.helper.StubData;
import OneCoin.Server.order.dto.OrderDto;
import OneCoin.Server.order.entity.Order;
import OneCoin.Server.order.mapper.OrderMapper;
import OneCoin.Server.order.service.OrderService;
import com.google.gson.Gson;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
@MockBean(JpaMetamodelMappingContext.class)
public class OrderControllerTest {

    private final String errorMessage = "limit과 market 중 하나의 필드는 반드시 값이 입력되어야 합니다. 또한 두 필드를 동시에 입력할 수 없습니다.";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrderService orderService;
    @MockBean
    private OrderMapper mapper;
    @Autowired
    private Gson gson;

    @Test
    @DisplayName("valid test: @MustHavePrice 테스트, 모든 필드가 0이면 에러가 발생한다.")
    void validTest1() throws Exception {
        OrderDto.Post postDto = StubData.MockOrderPostDto.getMockOrderPost();
        postDto.setLimit(0);
        String content = gson.toJson(postDto);

        mockMvc.perform(
                        post("/api/order/KRW-BTC")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andExpect(status().isBadRequest());
//                .andExpect(status().reason(errorMessage)); // TODO advice
    }

    @Test
    @DisplayName("valid test: @MustHavePrice 테스트, limit, market 모두 값이 입력되어 있으면 에러가 발생한다.")
    void validTest2() throws Exception {
        OrderDto.Post redisPostDto = StubData.MockOrderPostDto.getMockOrderPost();
        redisPostDto.setMarket(100);
        String content = gson.toJson(redisPostDto);

        mockMvc.perform(
                        post("/api/order/KRW-BTC")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andExpect(status().isBadRequest());
//                .andExpect(status().reason(errorMessage));
    }

    @Test
    @DisplayName("slice test: 매수/매도 주문")
    void postOrderTest() throws Exception {
        // given
        OrderDto.Post redisPostDto = StubData.MockOrderPostDto.getMockOrderPost();
        String content = gson.toJson(redisPostDto);

        given(mapper.postDtoToOrder(Mockito.any(OrderDto.Post.class))).willReturn(new Order());
        doNothing().when(orderService).createOrder(any(), anyString());

        // when then
        mockMvc.perform(
                        post("/api/order/KRW-BTC")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andExpect(status().isCreated());
    }
}
