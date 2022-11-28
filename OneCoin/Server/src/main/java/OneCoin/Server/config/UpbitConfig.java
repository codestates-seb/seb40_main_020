package OneCoin.Server.config;

import OneCoin.Server.upbit.websocket.listener.UpbitWebSocketListener;
import OneCoin.Server.upbit.entity.enums.CoinList;
import OneCoin.Server.upbit.service.UpbitHandlingService;
import OneCoin.Server.utils.JsonUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UpbitConfig {

    @Bean
    public OkHttpClient okHttpClient(JsonUtil jsonUtil, UpbitHandlingService upbitHandlingService) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url("wss://api.upbit.com/websocket/v1").build();

        UpbitWebSocketListener webSocketListener = new UpbitWebSocketListener(jsonUtil, upbitHandlingService);
        webSocketListener.setParameter(CoinList.CODES);
        okHttpClient.newWebSocket(request, webSocketListener);
        okHttpClient.dispatcher().executorService().shutdown();

        return okHttpClient;
    }
}
