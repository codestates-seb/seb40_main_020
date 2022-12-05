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

import java.util.ArrayList;
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
        numberOfChatsToCreate = 20L;
        chatRoomId = 1;
        sessionId = "abcd1234";
        for (long i = 1; i <= numberOfChatsToCreate; i++) {
            chatMessageRepository.save(webSocketTestUtils.makeChatMessage(i, chatRoomId));
        }
        chatRoomRepository.create(chatRoomId);
    }

    @AfterEach
    void deleteMessages() {
        chatMessageRepository.removeAllInChatRoom(chatRoomId);
        lastSentScoreRepository.delete(sessionId);
        chatMessageRdbRepository.deleteAll();

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
        Long userId = 2195L;
        chatService.saveInMemoryChatMessagesToRdb();
        chatMessageRepository.save(webSocketTestUtils.makeChatMessage(userId, chatRoomId));
        chatService.saveInMemoryChatMessagesToRdb();
        List<ChatMessage> allSavedMessages = chatMessageRdbRepository.findAll();
        assertThat(allSavedMessages.size())
                .isEqualTo(numberOfChatsToCreate.intValue() + 1);
    }

    @Test
    void getChatMessagesTest() {
        //given
        chatService.saveInMemoryChatMessagesToRdb();
        chatMessageRepository.removeAllInChatRoom(chatRoomId);
        for (long i = numberOfChatsToCreate + 1; i <= numberOfChatsToCreate + 20; i++) {
            chatMessageRepository.save(webSocketTestUtils.makeChatMessage(i, chatRoomId));
        }
        //when
        List<ChatMessage> receivedFromCacheFirst = chatService.getChatMessages(chatRoomId, sessionId);
        List<ChatMessage> receivedFromCacheSecond = chatService.getChatMessages(chatRoomId, sessionId);
        List<ChatMessage> receivedFromRdbFirst = chatService.getChatMessages(chatRoomId, sessionId);
        List<ChatMessage> receivedFromRdbSecond = chatService.getChatMessages(chatRoomId, sessionId);
        //then
        List<List<ChatMessage>> collection = new ArrayList<>();
        collection.add(receivedFromCacheFirst);
        collection.add(receivedFromCacheSecond);
        collection.add(receivedFromRdbFirst);
        collection.add(receivedFromRdbSecond);

        //각 조회가 내림차순인가?
        for (List<ChatMessage> messages : collection) {
            Long userIdFirst = messages.get(0).getUserId();
            Long userIdLast = messages.get(messages.size() - 1).getUserId();
            Long diff = userIdFirst - userIdLast;
            assertThat(diff)
                    .isPositive();
        }

        //각 조회 사이가 겹치지는 않는가?
        Long userIdBefore = null;
        for (List<ChatMessage> messages : collection) {
            Long userIdFirst = messages.get(0).getUserId();
            if (userIdBefore != null) {
                assertThat(userIdBefore - userIdFirst)
                        .isPositive();
            }
            userIdBefore = messages.get(messages.size() - 1).getUserId();
        }
    }
}
