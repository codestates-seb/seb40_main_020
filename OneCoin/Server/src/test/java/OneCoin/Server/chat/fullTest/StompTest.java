package OneCoin.Server.chat.fullTest;

import OneCoin.Server.chat.chatMessage.dto.ChatRequestDto;
import OneCoin.Server.chat.chatMessage.dto.ChatResponseDto;
import OneCoin.Server.chat.chatRoom.dto.ChatRoomDto;
import OneCoin.Server.chat.chatRoom.entity.ChatRoom;
import OneCoin.Server.chat.chatRoom.repository.ChatRoomRepository;
import OneCoin.Server.chat.constant.MessageType;
import OneCoin.Server.config.auth.jwt.JwtTokenizer;
import OneCoin.Server.user.entity.User;
import OneCoin.Server.user.repository.UserRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
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

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StompTest {

    @LocalServerPort
    private int port;
    private BlockingQueue<ChatResponseDto> messages;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private JwtTokenizer jwtTokenizer;
    private TestUtils testUtils;
    private String url;

    @BeforeEach
    public void setUp() {
        testUtils = new TestUtils(jwtTokenizer);
        RestAssured.port = port;
        url = "ws://localhost:" + port + "/ws/chat";
        messages = new LinkedBlockingDeque<>();
        유저_삽입();
        방_생성();
    }

    private void 유저_삽입() {
        userRepository.save(testUtils.makeUser("zoro"));
        userRepository.save(testUtils.makeUser("nami"));
        userRepository.save(testUtils.makeUser("loofy"));
        userRepository.save(testUtils.makeUser("sangdi"));
    }


    private void 방_생성() {
        chatRoomRepository.save(makeRoom("I will"));
        chatRoomRepository.save(makeRoom("be a"));
        chatRoomRepository.save(makeRoom("pirate King"));
    }

    private ChatRoom makeRoom(String name) {
        return ChatRoom.builder()
                .name(name)
                .nation("kr")
                .build();
    }

    @DisplayName("유저가 입장 -> 메시지, 유저 메시지 전송 -> 메시지, 유저 퇴장 -> 메시지")
    @Test
    void enterUserAndBroadCastMessage() throws InterruptedException, ExecutionException, TimeoutException {
        String message = "hello everyone";
        ChatRoom chatRoom = chatRoomRepository.findAll().get(0);
        User user = userRepository.findAll().get(0);
        ChatResponseDto expected = ChatResponseDto.builder()
                .message(message)
                .userDisplayName(user.getDisplayName())
                .build();
        ChatRequestDto chatRequestDto = ChatRequestDto.builder()
                .chatRoomId(chatRoom.getChatRoomId())
                .userId(user.getUserId())
                .type(MessageType.TALK)
                .userDisplayName(user.getDisplayName())
                .message(message)
                .build();
        String jwt = "Bearer " + testUtils.generateAccessToken(user);

        // Settings
        WebSocketStompClient webSocketStompClient = 웹_소켓_STOMP_CLIENT();
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());

        // Connection
        ListenableFuture<StompSession> connect = webSocketStompClient
                .connect(url,
                        new WebSocketHttpHeaders(),
                        getStompHeadersWithAuthorization(jwt),
                        new StompSessionHandlerAdapter() {
                        });
        StompSession stompSession = connect.get(100, TimeUnit.SECONDS);

        stompSession.subscribe(String.format("/topic/rooms/%d", chatRoom.getChatRoomId()), new StompFrameHandlerImpl(new ChatResponseDto(), messages));
        ChatResponseDto response1 = messages.poll(5, TimeUnit.SECONDS);
        stompSession.send("/app/rooms", chatRequestDto);
        ChatResponseDto response2 = messages.poll(5, TimeUnit.SECONDS);
        stompSession.disconnect();
        ChatResponseDto response3 = messages.poll(5, TimeUnit.SECONDS);

        // Then
        List<ChatRoomDto> chatRoomDtos = chatRoomRepository.findAllRegisteredUserNumberGroupByChatRoom();
        ChatRoomDto chatRoomDto = chatRoomDtos.stream()
                .filter(dto -> dto.getChatRoomId().equals(chatRoom.getChatRoomId()))
                .findAny().get();


        assertThat(response1.getMessage()).contains("입장");
        assertThat(response2.getMessage()).isEqualTo(message);
        assertThat(response3).isNull();
        assertThat(chatRoomDto.getNumberOfChatters()).isEqualTo(0);
    }

    private StompHeaders getStompHeadersWithAuthorization(String jwt) {
        StompHeaders stompHeaders = new StompHeaders();
        stompHeaders.add("Authorization", jwt);
        return stompHeaders;
    }

    private WebSocketStompClient 웹_소켓_STOMP_CLIENT() {
        StandardWebSocketClient standardWebSocketClient = new StandardWebSocketClient();
        WebSocketTransport webSocketTransport = new WebSocketTransport(standardWebSocketClient);
        List<Transport> transports = Collections.singletonList(webSocketTransport);
        SockJsClient sockJsClient = new SockJsClient(transports);

        return new WebSocketStompClient(sockJsClient);
    }

    public class StompFrameHandlerImpl<T> implements StompFrameHandler {

        private final T response;
        private final BlockingQueue<T> responses;

        public StompFrameHandlerImpl(final T response, final BlockingQueue<T> responses) {
            this.response = response;
            this.responses = responses;
        }

        @Override
        public Type getPayloadType(final StompHeaders headers) {
            return response.getClass();
        }

        @Override
        public void handleFrame(final StompHeaders headers, final Object payload) {
            System.out.println(payload);
            responses.offer((T) payload);
        }
    }
}
