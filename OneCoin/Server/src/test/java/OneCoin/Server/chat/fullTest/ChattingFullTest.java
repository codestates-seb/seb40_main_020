package OneCoin.Server.chat.fullTest;

import OneCoin.Server.chat.chatMessage.dto.ChatRequestDto;
import OneCoin.Server.chat.chatMessage.dto.ChatResponseDto;
import OneCoin.Server.chat.chatMessage.repository.ChatMessageRepository;
import OneCoin.Server.chat.chatRoom.entity.UserInChatRoom;
import OneCoin.Server.chat.chatRoom.repository.ChatRoomRepository;
import OneCoin.Server.chat.chatRoom.repository.UserInChatRoomRepository;
import OneCoin.Server.chat.chatRoom.service.ChatRoomService;
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
    private TestUtils testUtils;
    private String url;
    private BlockingQueue<ChatResponseDto> receivedMessagesOfSender;
    private BlockingQueue<ChatResponseDto> receivedMessagesOfReceiver;

    @BeforeEach
    public void setUp() {
        testUtils = new TestUtils(jwtTokenizer, chatRoomRepository, userInChatRoomRepository, chatMessageRepository, userRepository, redisTemplate);
        chatRoomId = 1;
        testUtils.clearInMemory(chatRoomId);
        RestAssured.port = port;
        url = "ws://localhost:" + port + "/ws/chat";
        receivedMessagesOfSender = new LinkedBlockingDeque<>();
        receivedMessagesOfReceiver = new LinkedBlockingDeque<>();
        testUtils.saveUsers();
    }
    @DisplayName("유저가 입장 -> 메시지, 유저 메시지 전송 -> 메시지, 유저 퇴장 -> 메시지")
    @Test
    void enterUserAndBroadCastMessage() throws InterruptedException, ExecutionException, TimeoutException {
        /** user Setting */
        User sendingUser = userRepository.findAll().get(0);
        String accessToken = testUtils.generateAccessToken(sendingUser);

        /** message Setting */
        String message = "hello everyone";
        ChatRequestDto sendingMessage = testUtils.makeSendingMessage(message, chatRoomId, sendingUser);
        /** Connection for receiver, NonAuthenticatedUser*/
        WebSocketStompClient receiverStompClient = testUtils.makeStompClient();
        StompSession receiver =
                testUtils.getSessionAfterConnect(receiverStompClient, url, new WebSocketHttpHeaders(), new StompHeaders());

        /** Connection for sender, AuthenticatedUser */
        WebSocketStompClient senderStompClient = testUtils.makeStompClient();
        StompHeaders senderStompHeaders = testUtils.makeStompHeadersWithAccessToken(accessToken);
        StompSession sender = testUtils.getSessionAfterConnect(senderStompClient, url,  new WebSocketHttpHeaders(), senderStompHeaders);

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

        /** sender : 비정상적인 연결 종료시 */
        senderStompClient.stop();
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
        assertThat(userCount).isEqualTo(1);
//        assertThat(receiverMsgAfterSenderConnectionTerminated.getMessage()).contains("퇴장");
//        assertThat(senderMsgAfterSenderConnectionTerminated).isNull();
        assertThat(numberOfChattersLast).isEqualTo(1L);
        assertThat(usersLast.size()).isEqualTo(0L);
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
