package OneCoin.Server.chat.controller;

import OneCoin.Server.chat.controller.NumberOfChattersScheduler;
import OneCoin.Server.chat.dto.ChatResponseDto;
import OneCoin.Server.chat.entity.ChatRoom;
import OneCoin.Server.chat.testUtil.StompFrameHandlerImpl;
import OneCoin.Server.chat.testUtil.WebSocketTestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NumberOfChattersSchedulerTest {
    @Autowired
    private NumberOfChattersScheduler scheduler;
    @LocalServerPort
    private int port;
    @Autowired
    private WebSocketTestUtils webSocketTestUtils;
    @Autowired
    private ObjectMapper objectMapper;
    private String url;
    private BlockingQueue<List<ChatRoom>> receivedMessages;
    private BlockingQueue<ChatResponseDto> receivedMessagesOfChat;
    @BeforeAll
    public void setUp() throws ExecutionException, InterruptedException, TimeoutException {
        RestAssured.port = port;
        url = "ws://localhost:" + port + "/ws/chat";
        receivedMessages = new LinkedBlockingDeque<>();

        WebSocketStompClient stompClientForChat = webSocketTestUtils.makeStompClient();
        StompSession receiver =
                webSocketTestUtils.getSessionAfterConnect(stompClientForChat, url, new WebSocketHttpHeaders(), new StompHeaders());
        receiver.subscribe("/topic/rooms/1", new StompFrameHandlerImpl(new ChatResponseDto(), receivedMessagesOfChat));
    }

    @Test
    void sendTest() throws ExecutionException, InterruptedException, TimeoutException {
        WebSocketStompClient stompClient = webSocketTestUtils.makeStompClient();
        //given: connect
        StompSession receiver =
                webSocketTestUtils.getSessionAfterConnect(stompClient, url, new WebSocketHttpHeaders(), new StompHeaders());
        //when : subscribe
        receiver.subscribe("/topic/rooms-info", new StompFrameHandlerImpl(new ArrayList<ChatRoom>(), receivedMessages));
        List<ChatRoom> receivedMessage = Arrays.asList(objectMapper.convertValue(receivedMessages.poll(1, TimeUnit.SECONDS), ChatRoom[].class));

        //then
        assertThat(receivedMessage.get(0).getNumberOfChatters())
                .isEqualTo(1);
    }

}
