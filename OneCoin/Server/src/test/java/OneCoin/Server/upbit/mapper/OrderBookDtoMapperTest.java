package OneCoin.Server.upbit.mapper;

import OneCoin.Server.helper.StubData;
import OneCoin.Server.upbit.dto.orderbook.OrderBookDto;
import OneCoin.Server.upbit.entity.UnitInfo;
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

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@MockBean(OkHttpClient.class) // webSocket 끄기
public class OrderBookDtoMapperTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JsonUtil jsonUtil;
    @Autowired
    private OrderBookDtoMapper mapper;
    private List<UnitInfo> unitInfos;

    @BeforeEach
    void createOrderBook() throws JsonProcessingException {
        // given
        String json = StubData.MockUpbitAPI.getJsonOrderBook();
        JsonNode jsonNode = jsonUtil.fromJson(json, JsonNode.class);
        unitInfos = Arrays.asList(objectMapper.readValue(jsonNode.get("orderbook_units").toString(), UnitInfo[].class));
    }

    @Test
    @DisplayName("매수 호가(15개), 매도 호가(15개) 정보를 매핑한다.")
    void mappingTest() {
        // when
        OrderBookDto orderBookDto = mapper.unitInfoToOrderBookDto(unitInfos, "20000000");

        // then
        assertThat(orderBookDto.getAskInfo().get(0).getAskPrice()).isEqualTo("2.2525E7");
        assertThat(orderBookDto.getAskInfo().get(0).getAskSize()).isEqualTo("7.83E-5");
        assertThat(orderBookDto.getAskInfo().size()).isEqualTo(15);
        assertThat(orderBookDto.getBidInfo().get(14).getBidPrice()).isEqualTo("2.2497E7");
        assertThat(orderBookDto.getBidInfo().get(14).getBidSize()).isEqualTo("7.56324623");
        assertThat(orderBookDto.getBidInfo().size()).isEqualTo(15);
    }
}
