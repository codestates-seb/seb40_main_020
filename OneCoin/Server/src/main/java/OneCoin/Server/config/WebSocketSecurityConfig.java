package OneCoin.Server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

import static org.springframework.messaging.simp.SimpMessageType.MESSAGE;
import static org.springframework.messaging.simp.SimpMessageType.SUBSCRIBE;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        //TODO: 비공개 채팅방 개설시 수정 필요
        messages
                .simpSubscribeDestMatchers("/topic/**").permitAll() //구독은 누구나
                .simpDestMatchers("/app/**").hasRole("USER"); //발행은 유저만
    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
