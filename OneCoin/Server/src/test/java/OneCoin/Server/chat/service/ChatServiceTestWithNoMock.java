package OneCoin.Server.chat.service;

import OneCoin.Server.chat.entity.ChatMessage;
import OneCoin.Server.chat.repository.ChatMessageRdbRepository;
import OneCoin.Server.chat.repository.ChatMessageRepository;
import OneCoin.Server.chat.repository.ChatRoomRepository;
import OneCoin.Server.chat.repository.LastSentScoreRepository;
import OneCoin.Server.chat.testUtil.WebSocketTestUtils;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class ChatServiceTestWithNoMock {
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private LastSentScoreRepository lastSentScoreRepository;
    @Autowired
    private WebSocketTestUtils webSocketTestUtils;
    @Autowired
    private ChatService chatService;
    @Autowired
    private ChatMessageRdbRepository chatMessageRdbRepository;
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    private Long numberOfChatsToCreate;
    private Integer chatRoomId;
    private String sessionId;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @BeforeEach
    void saveMessages() {
        numberOfChatsToCreate = 120L;
        chatRoomId = 1;
        sessionId = "abcd1234";
        for (long i = 1; i <= numberOfChatsToCreate; i++) {
            chatMessageRepository.save(webSocketTestUtils.makeChatMessage(i, chatRoomId));
        }
    }

    @AfterEach
    void deleteMessages() {
        chatMessageRepository.removeAllInChatRoom(chatRoomId);
        lastSentScoreRepository.delete(sessionId);

    }

    @Test
    void getMessagesFromRoomTest_더_과거조회() {
        //given
        Long limit = chatMessageRepository.getNUMBER_OF_CHATS_TO_SHOW();
        List<ChatMessage> messagesReceivedFirst = chatService.getChatMessages(chatRoomId, sessionId);
        List<ChatMessage> messagesReceivedSecond = chatService.getChatMessages(chatRoomId, sessionId);
        //then
        Long lastUserIdOfFirstReceivedMessages = messagesReceivedFirst.get(messagesReceivedFirst.size() - 1).getUserId();
        Long firstUserIdOfSecondReceivedMessages = messagesReceivedSecond.get(0).getUserId();
        Long diff = lastUserIdOfFirstReceivedMessages - firstUserIdOfSecondReceivedMessages;
        assertThat(messagesReceivedSecond.size())
                .isCloseTo(limit.intValue(), Percentage.withPercentage(50));
        assertThat(diff)
                .isNotEqualTo(0L);
        assertThat(diff)
                .isPositive();
    }

    @Test
    void saveInMemoryChatMessagesToRDBTest() {
        //given
        chatRoomRepository.create(chatRoomId);
        Long userId = 2195L;
        chatService.saveInMemoryChatMessagesToRdb();
        chatMessageRepository.save(webSocketTestUtils.makeChatMessage(userId, chatRoomId));
        chatService.saveInMemoryChatMessagesToRdb();
        List<ChatMessage> allSavedMessages = chatMessageRdbRepository.findAll();
        assertThat(allSavedMessages.size())
                .isEqualTo(numberOfChatsToCreate.intValue() + 1);
    }
}
