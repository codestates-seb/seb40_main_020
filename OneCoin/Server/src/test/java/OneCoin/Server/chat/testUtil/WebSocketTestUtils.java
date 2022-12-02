package OneCoin.Server.chat.testUtil;

import OneCoin.Server.chat.constant.MessageType;
import OneCoin.Server.chat.dto.ChatRequestDto;
import OneCoin.Server.chat.entity.ChatMessage;
import OneCoin.Server.chat.repository.ChatMessageRepository;
import OneCoin.Server.chat.repository.ChatRoomRepository;
import OneCoin.Server.chat.repository.UserInChatRoomRepository;
import OneCoin.Server.config.auth.jwt.JwtTokenizer;
import OneCoin.Server.user.entity.Platform;
import OneCoin.Server.user.entity.Role;
import OneCoin.Server.user.entity.User;
import OneCoin.Server.user.repository.UserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
@Component
public class WebSocketTestUtils {
    private final JwtTokenizer jwtTokenizer;
    private final ChatRoomRepository chatRoomRepository;
    private final UserInChatRoomRepository userInChatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final RedisTemplate redisTemplate;

    public WebSocketTestUtils(JwtTokenizer jwtTokenizer, ChatRoomRepository chatRoomRepository, UserInChatRoomRepository userInChatRoomRepository, ChatMessageRepository chatMessageRepository, UserRepository userRepository, RedisTemplate redisTemplate) {
        this.jwtTokenizer = jwtTokenizer;
        this.chatRoomRepository = chatRoomRepository;
        this.userInChatRoomRepository = userInChatRoomRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
    }

    public String generateAccessToken(User user) {
        // userInfo
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getUserId());
        claims.put("username", user.getEmail());
        claims.put("roles", user.getUserRole());
        claims.put("displayName", user.getDisplayName());

        String subject = user.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());

        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        return "Bearer " + jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);
    }

    public User makeUser(String name) {
        return User.builder()
                .userRole(Role.ROLE_USER)
                .displayName(name)
                .email(name + "@google.com")
                .password(name + "1234!@")
                .platform(Platform.KAKAO)
                .build();
    }

    public void clearInMemory(Integer chatRoomId) {
        chatRoomRepository.delete(chatRoomId);
        userInChatRoomRepository.removeAllInChatRoom(chatRoomId);
        chatMessageRepository.removeAllInChatRoom(chatRoomId);
    }

    public void saveUsers() {
        userRepository.save(makeUser("zoro"));
        userRepository.save(makeUser("nami"));
        userRepository.save(makeUser("loofy"));
        userRepository.save(makeUser("sangdi"));

    }

    public StompSession getSessionAfterConnect(WebSocketStompClient stompClient, String url, WebSocketHttpHeaders httpHeaders, StompHeaders stompHeaders) throws ExecutionException, InterruptedException, TimeoutException {
        ListenableFuture<StompSession> connection = stompClient
                .connect(url, httpHeaders, stompHeaders, new StompSessionHandlerAdapter() {
                });
        return connection.get(100, TimeUnit.SECONDS);
    }

    public StompHeaders makeStompHeadersWithAccessToken(String accessToken) {
        StompHeaders headers = new StompHeaders();
        headers.set("Authorization", accessToken);
        return headers;
    }

    public ChatRequestDto makeSendingMessage(String message, Integer chatRoomId, User sendingUser) {
        return ChatRequestDto.builder()
                .chatRoomId(chatRoomId)
                .message(message)
                .build();
    }

    public WebSocketStompClient makeStompClient() {
        StandardWebSocketClient standardWebSocketClient = new StandardWebSocketClient();
        WebSocketTransport webSocketTransport = new WebSocketTransport(standardWebSocketClient);
        List<Transport> transports = List.of(webSocketTransport);
        SockJsClient sockJsClient = new SockJsClient(transports);

        WebSocketStompClient client = new WebSocketStompClient(sockJsClient);
        client.setMessageConverter(new MappingJackson2MessageConverter());
        return client;
    }

    public WebSocketHttpHeaders makeHttpHeadersWithJwt(String jwt) {
        WebSocketHttpHeaders httpHeaders = new WebSocketHttpHeaders();
        httpHeaders.add("Authorization", jwt);
        return httpHeaders;
    }

    public ChatMessage makeChatMessage(long userId, Integer chatRoomId) {
        return new ChatMessage(
                null,
                MessageType.TALK,
                "hello" + userId,
                LocalDateTime.now().toString(),
                userId,
                chatRoomId,
                String.valueOf(userId * 100L));
    }

}
