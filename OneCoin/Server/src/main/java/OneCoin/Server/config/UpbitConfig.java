package OneCoin.Server.config;

import OneCoin.Server.upbit.api.UpbitWebSocketListener;
import OneCoin.Server.upbit.entity.SiseType;
import OneCoin.Server.upbit.utils.JsonUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class UpbitConfig {


    @Bean
    public OkHttpClient okHttpClient(JsonUtil jsonUtil) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url("wss://api.upbit.com/websocket/v1").build();

        UpbitWebSocketListener webSocketListener = new UpbitWebSocketListener(jsonUtil);
        webSocketListener.setParameter(SiseType.TRADE, List.of("KRW-BTC")); // TODO
        okHttpClient.newWebSocket(request, webSocketListener);
        okHttpClient.dispatcher().executorService().shutdown();

        return okHttpClient;
    }
}
