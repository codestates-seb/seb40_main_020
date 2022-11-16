package OneCoin.Server.webSocket.config;

import OneCoin.Server.webSocket.service.ChatService;
import OneCoin.Server.webSocket.handler.CustomWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

//웹소켓 핸들러를 특정 url과 매핑시킴
@Configuration
@EnableWebSocket
@AllArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ChatService chatService;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(customHandler(), "/ws/chat")
                .setAllowedOrigins("*");
    }
    @Bean
    public WebSocketHandler customHandler() {
        return new CustomWebSocketHandler(objectMapper, chatService);
    }
}
