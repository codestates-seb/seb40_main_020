package OneCoin.Server.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JsonUtil {
    private final ObjectMapper objectMapper;

    public <T> String toJson(T data) {
        return objectMapper.valueToTree(data).toString();
    }

    public <T> T fromJson(String json, Class<T> classType) {
        try {
            return objectMapper.treeToValue(objectMapper.readTree(json), classType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
