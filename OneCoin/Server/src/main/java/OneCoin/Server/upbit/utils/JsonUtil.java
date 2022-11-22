package OneCoin.Server.upbit.utils;

import OneCoin.Server.upbit.entity.Trade;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class JsonUtil {
    private ObjectMapper objectMapper = customObjectMapper();

    public <T> String toJson(T data) {
        return objectMapper.valueToTree(data).toString();
    }

    public <T> T fromJson(String json, Class<T> classType) {
        try {
//            Trade trade = objectMapper.readValue(json, Trade.class); TODO
            return objectMapper.treeToValue(objectMapper.readTree(json), classType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    private ObjectMapper customObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY); // serializer는 private 필드에 접근 못하므로 해당 설정필요 or @JsonProperty
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // api에서 정해진 키를 다 파싱하지 않음 or @JsonIgnoreProperties
        return objectMapper;
    }
}
