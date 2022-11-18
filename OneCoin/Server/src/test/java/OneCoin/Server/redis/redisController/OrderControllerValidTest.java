package OneCoin.Server.redis.redisController;

import OneCoin.Server.helper.StubData;
import OneCoin.Server.order.dto.RedisOrderDto;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerValidTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    private final String errorMessage = "Limit과 Market 중 한 필드는 반드시 값이 입력되어야 합니다. 또한 한 번에 두 필드 모두 입력할 수 없습니다.";

    @SneakyThrows
    @Test
    @DisplayName("valid test: @MustHaveLimitOrMarket 테스트, 두 필드 모두 0이면 에러가 발생한다.")
    void validTest1() {
        RedisOrderDto.Post redisPostDto = StubData.MockRedisPostDto.getMockRedisPost();
        redisPostDto.setLimit(0);
        String content = gson.toJson(redisPostDto);

        mockMvc.perform(
                        post("/api/order/KRW-BTC")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andExpect(status().isBadRequest());
//                .andExpect(status().reason(errorMessage)); // TODO advice
    }

    @SneakyThrows
    @Test
    @DisplayName("valid test: @MustHaveLimitOrMarket 테스트, 두 필드 모두 값이 있으면 에러가 발생한다.")
    void validTest2() {
        RedisOrderDto.Post redisPostDto = StubData.MockRedisPostDto.getMockRedisPost();
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

    @SneakyThrows
    @Test
    @DisplayName("valid test: @MustHaveLimitOrMarket 테스트, 두 필드 중 하나만 값이 있으면 에러가 발생하지 않는다.")
    void validTest3() {
        RedisOrderDto.Post redisPostDto = StubData.MockRedisPostDto.getMockRedisPost();
        String content = gson.toJson(redisPostDto);

        mockMvc.perform(
                        post("/api/order/KRW-BTC")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                )
                .andExpect(status().isCreated());
    }
}
