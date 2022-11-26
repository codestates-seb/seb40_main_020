package OneCoin.Server.chat.fullTest;

import OneCoin.Server.chat.chatMessage.dto.ChatRequestDto;
import OneCoin.Server.chat.chatRoom.repository.ChatRoomInMemoryRepository;
import OneCoin.Server.chat.chatRoom.repository.UserInChatRoomRepository;
import OneCoin.Server.chat.constant.MessageType;
import OneCoin.Server.config.auth.jwt.JwtTokenizer;
import OneCoin.Server.user.entity.Platform;
import OneCoin.Server.user.entity.Role;
import OneCoin.Server.user.entity.User;
import OneCoin.Server.user.repository.UserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TestUtils {
    private final JwtTokenizer jwtTokenizer;
    private final ChatRoomInMemoryRepository chatRoomInMemoryRepository;
    private final UserInChatRoomRepository userInChatRoomRepository;
    private final UserRepository userRepository;
    private final RedisTemplate redisTemplate;

    public TestUtils(JwtTokenizer jwtTokenizer, ChatRoomInMemoryRepository chatRoomInMemoryRepository,
                     UserInChatRoomRepository userInChatRoomRepository, UserRepository userRepository,
                     RedisTemplate redisTemplate) {
        this.jwtTokenizer = jwtTokenizer;
        this.chatRoomInMemoryRepository = chatRoomInMemoryRepository;
        this.userInChatRoomRepository = userInChatRoomRepository;
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
    }

    public String generateAccessToken(User user) {
        // userInfo
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getUserId());
        claims.put("username", user.getEmail());
        claims.put("roles", user.getUserRole());

        String subject = user.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());

        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        return jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);
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

    public void clearInMemory(Long chatRoomId) {
        chatRoomInMemoryRepository.deleteAll();
        userInChatRoomRepository.deleteAll();
        String key = "ChatRoom" + String.valueOf(chatRoomId) + "Message";
        ZSetOperations<String, Object> operations = redisTemplate.opsForZSet();
        operations.removeRange(key, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    public void saveUsers() {
        userRepository.save(makeUser("zoro"));
        userRepository.save(makeUser("nami"));
        userRepository.save(makeUser("loofy"));
        userRepository.save(makeUser("sangdi"));

    }
    public StompSession getSessionAfterConnect(String url, WebSocketHttpHeaders httpHeaders, StompHeaders stompHeaders) throws ExecutionException, InterruptedException, TimeoutException {
        WebSocketStompClient stompClient = makeStompClient();
        ListenableFuture<StompSession> connection = stompClient
                .connect(url, httpHeaders, stompHeaders, new StompSessionHandlerAdapter() {
                });
        return connection.get(100, TimeUnit.SECONDS);
    }

    public ChatRequestDto makeSendingMessage(String message, Long chatRoomId, User sendingUser) {
        return ChatRequestDto.builder()
                .chatRoomId(chatRoomId)
                .userId(sendingUser.getUserId())
                .type(MessageType.TALK)
                .userDisplayName(sendingUser.getDisplayName())
                .message(message)
                .build();
    }

    public StompHeaders makeStompHeaderWithJwt(String jwt) {
        StompHeaders stompHeaders = new StompHeaders();
        stompHeaders.add("Authorization", jwt);
        return stompHeaders;
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
}
