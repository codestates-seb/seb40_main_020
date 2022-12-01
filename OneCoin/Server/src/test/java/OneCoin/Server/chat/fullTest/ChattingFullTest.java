package OneCoin.Server.chat.fullTest;

import OneCoin.Server.chat.dto.ChatRequestDto;
import OneCoin.Server.chat.dto.ChatResponseDto;
import OneCoin.Server.chat.repository.ChatMessageRepository;
import OneCoin.Server.chat.entity.UserInChatRoom;
import OneCoin.Server.chat.repository.ChatRoomRepository;
import OneCoin.Server.chat.repository.UserInChatRoomRepository;
import OneCoin.Server.chat.service.ChatRoomService;
import OneCoin.Server.chat.testUtil.StompFrameHandlerImpl;
import OneCoin.Server.chat.testUtil.WebSocketTestUtils;
import OneCoin.Server.config.auth.jwt.JwtTokenizer;
import OneCoin.Server.user.entity.User;
import OneCoin.Server.user.repository.UserRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChattingFullTest {

    @LocalServerPort
    private int port;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenizer jwtTokenizer;
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private UserInChatRoomRepository userInChatRoomRepository;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private SimpUserRegistry simpUserRegistry;
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    private Integer chatRoomId;
    private WebSocketTestUtils webSocketTestUtils;
    private String url;
    private BlockingQueue<ChatResponseDto> receivedMessagesOfSender;
    private BlockingQueue<ChatResponseDto> receivedMessagesOfReceiver;

    @BeforeEach
    public void setUp() {
        webSocketTestUtils = new WebSocketTestUtils(jwtTokenizer, chatRoomRepository, userInChatRoomRepository, chatMessageRepository, userRepository, redisTemplate);
        chatRoomId = 1;
        webSocketTestUtils.clearInMemory(chatRoomId);
        RestAssured.port = port;
        url = "ws://localhost:" + port + "/ws/chat";
        receivedMessagesOfSender = new LinkedBlockingDeque<>();
        receivedMessagesOfReceiver = new LinkedBlockingDeque<>();
        webSocketTestUtils.saveUsers();
    }

    /**
     * 테스트 시나리오
     * 유저 : 2 명 (로그인 유저, 비로그인 유저)
     * 1. 비로그인(receiver), 로그인(sender) 유저 : 웹소켓 연결 (Connection) -> No messages received
     * 2. 비로그인 유저 : 채팅 방 입장 (Subscribe) -> No messages received
     * 3. 로그인 유저 : 채팅 방 입장 (Subscribe) -> Both receives an Enter message
     * 4. 로그인 유저 : 메시지 전송 (Send)  -> Both receives an Send message
     * 5. 로그인 유저 : 채팅방 퇴장 (Unsubscribe) -> The unLogged-In user receives an Leave message
     * 6. 로그인 유저 : 채팅방 재입장 (Subscribe) -> Both receives an Enter message ( But No test with it)
     * 7. 로그인 유저 : 웹소켓 종료 (Disconnect) -> The unLogged-In user receives an Leave message
     */
    @Test
    void chatScenarioTest() throws InterruptedException, ExecutionException, TimeoutException {
        /** user Setting */
        User sendingUser = userRepository.findAll().get(0);
        String accessToken = webSocketTestUtils.generateAccessToken(sendingUser);

        /** message Setting */
        String message = "hello everyone";
        ChatRequestDto sendingMessage = webSocketTestUtils.makeSendingMessage(message, chatRoomId, sendingUser);
        /** Connection for receiver, NonAuthenticatedUser*/
        WebSocketStompClient receiverStompClient = webSocketTestUtils.makeStompClient();
        StompSession receiver =
                webSocketTestUtils.getSessionAfterConnect(receiverStompClient, url, new WebSocketHttpHeaders(), new StompHeaders());

        /** Connection for sender, AuthenticatedUser */
        WebSocketStompClient senderStompClient = webSocketTestUtils.makeStompClient();
        StompHeaders senderStompHeaders = webSocketTestUtils.makeStompHeadersWithAccessToken(accessToken);
        StompSession sender = webSocketTestUtils.getSessionAfterConnect(senderStompClient, url,  new WebSocketHttpHeaders(), senderStompHeaders);

        /** receiver : Subscribe */
        receiver.subscribe(String.format("/topic/rooms/%d", chatRoomId), new StompFrameHandlerImpl(new ChatResponseDto(), receivedMessagesOfReceiver));
        /** then */
        ChatResponseDto receiverMsgAfterReceiverSubscribe = receivedMessagesOfReceiver.poll(3, TimeUnit.SECONDS);
        ChatResponseDto senderMsaAfterReceiverSubscribe = receivedMessagesOfSender.poll(1, TimeUnit.SECONDS);

        /** sender : Subscribe */
        StompSession.Subscription subscription = sender.subscribe(String.format("/topic/rooms/%d", chatRoomId), new StompFrameHandlerImpl(new ChatResponseDto(), receivedMessagesOfSender));
        /** then : received Messages */
        ChatResponseDto receiverMsgAfterSenderSubscribe = receivedMessagesOfReceiver.poll(1, TimeUnit.SECONDS);
        ChatResponseDto senderMsgAfterSenderSubscribe = receivedMessagesOfSender.poll(1, TimeUnit.SECONDS);

        /** then : Number Of Chatters */
        long numberOfChattersFirst = chatRoomService.getNumberOfUserInChatRoom(chatRoomId);

        /** then : authenticated Users In ChatRoom */
        List<UserInChatRoom> usersFirst = chatRoomService.findUsersInChatRoom(chatRoomId);

        /** sender : send Message */
        sender.send("/app/rooms", sendingMessage);
        /** then : received Messages */
        ChatResponseDto receiverMsgAfterSenderSend = receivedMessagesOfReceiver.poll(1, TimeUnit.SECONDS);
        ChatResponseDto senderMsgAfterSenderSend = receivedMessagesOfSender.poll(1, TimeUnit.SECONDS);

        /** sender : Unsubscribe */
        subscription.unsubscribe();
        /** then : received Messages */
        ChatResponseDto receiverMsgAfterSenderUnsubscribe = receivedMessagesOfReceiver.poll(1, TimeUnit.SECONDS);
        ChatResponseDto senderMsgAfterSenderUnsubscribe = receivedMessagesOfSender.poll(1, TimeUnit.SECONDS);

        /** then : Number Of Chatters */
        long numberOfChattersLast = chatRoomService.getNumberOfUserInChatRoom(chatRoomId);

        /** then : authenticated Users In ChatRoom */
        List<UserInChatRoom> usersLast = chatRoomService.findUsersInChatRoom(chatRoomId);

        /** sender : Subscribe again */
        sender.subscribe(String.format("/topic/rooms/%d", chatRoomId), new StompFrameHandlerImpl(new ChatResponseDto(), receivedMessagesOfSender));
        receivedMessagesOfReceiver.poll(1, TimeUnit.SECONDS);
        receivedMessagesOfSender.poll(1, TimeUnit.SECONDS);


         /** sender : Disconnect */
        sender.disconnect();
        ChatResponseDto receiverMsgAfterSenderConnectionTerminated = receivedMessagesOfReceiver.poll(1, TimeUnit.SECONDS);
        ChatResponseDto senderMsgAfterSenderConnectionTerminated = receivedMessagesOfSender.poll(1, TimeUnit.SECONDS);
        int userCount = simpUserRegistry.getUserCount();

        /** Then : assert */
        /** After Receiver Subscribe */
        assertThat(receiverMsgAfterReceiverSubscribe).isNull();
        assertThat(senderMsaAfterReceiverSubscribe).isNull();
        /** After Sender Subscribe */
        assertThat(receiverMsgAfterSenderSubscribe.getMessage()).contains("입장");
        assertThat(senderMsgAfterSenderSubscribe.getMessage()).contains("입장");
        assertThat(numberOfChattersFirst).isEqualTo(2L);
        assertThat(usersFirst.size()).isEqualTo(1L);
        /** After Sender Send a Message */
        assertThat(receiverMsgAfterSenderSend.getMessage()).isEqualTo(message);
        assertThat(senderMsgAfterSenderSend.getMessage()).isEqualTo(message);
        /** After Sender UnSubscribe */
        assertThat(receiverMsgAfterSenderUnsubscribe.getMessage()).contains("퇴장");
        assertThat(senderMsgAfterSenderUnsubscribe).isNull();
        assertThat(numberOfChattersLast).isEqualTo(1L);
        assertThat(usersLast.size()).isEqualTo(0L);
        /** After Sender's Connection Terminated */
        assertThat(userCount).isEqualTo(0);
        assertThat(receiverMsgAfterSenderConnectionTerminated.getMessage()).contains("퇴장");
        assertThat(senderMsgAfterSenderConnectionTerminated).isNull();
    }
}
