package OneCoin.Server.chat.repository;

import OneCoin.Server.chat.entity.ChatMessage;
import OneCoin.Server.chat.testUtil.WebSocketTestUtils;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChatMessageRepositoryImpleTest {
    @Autowired
    private ChatMessageRepositoryImple chatMessageRepository;
    @Autowired
    private LastSentScoreRepository lastSentScoreRepository;
    @Autowired
    private WebSocketTestUtils webSocketTestUtils;
    private Long numberOfChatsToCreate;
    private Integer chatRoomId;
    private String sessionId;

    @BeforeEach
    void saveMessages() {
        numberOfChatsToCreate = 120L;
        chatRoomId = 1000;
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
    void getMessagesFromRoomTest_최초입장시() {
        //given
        Long limit = chatMessageRepository.getNUMBER_OF_CHATS_TO_SHOW();
        //when
        List<ChatMessage> chatMessageList = chatMessageRepository.getMessagesFromRoom(chatRoomId, sessionId);
        //then
        assertThat(chatMessageList.size())
                .isCloseTo(limit.intValue(), Percentage.withPercentage(50));
        assertThat(chatMessageList.get(0).getUserId())
                .isEqualTo(numberOfChatsToCreate);
    }

    @Test
    void getMessagesFromRoomTest_더_과거조회() {
        //given
        Long limit = chatMessageRepository.getNUMBER_OF_CHATS_TO_SHOW();
        List<ChatMessage> messagesReceivedFirst = chatMessageRepository.getMessagesFromRoom(chatRoomId, sessionId);
        List<ChatMessage> messagesReceivedSecond = chatMessageRepository.getMessagesFromRoom(chatRoomId, sessionId);
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

//    @Test
//    void findAllTest() {
//        //when
//        List<ChatMessage> chatRoomList = chatMessageRepository.findAll(chatRoomId);
//        //then
//        assertThat(chatRoomList.size())
//                .isEqualTo(numberOfChatsToCreate.intValue());
//    }
//
//    @Test
//    void getIndexTest() {
//        //given
//        List<ChatMessage> chatMessageList = chatMessageRepository.getMessagesFromRoom(chatRoomId);
//        ChatMessage latest = chatMessageList.get(0);
//        long numberOfChatsToCreate = 10;
//        for (long i = 1; i <= numberOfChatsToCreate; i++) {
//            chatMessageRepository.save(webSocketTestUtils.makeChatMessage(i, chatRoomId));
//        }
//
//        //when
//        Long index = chatMessageRepository.getIndex(chatRoomId, latest);
//
//        //then
//        assertThat(index)
//                .isEqualTo(numberOfChatsToCreate);
//    }
//
//    @Test
//    void findAllAfter() {
//        //given
//        Long index = 10L;
//        //when
//        List<ChatMessage> all = chatMessageRepository.findAll(chatRoomId);
//        List<ChatMessage> chatMessageList = chatMessageRepository.findAllAfter(chatRoomId, index);
//
//        ChatMessage chatMessageAtIndexPlusOne = all.get(index.intValue() + 1);
//        ChatMessage lastMessageRetrieve = chatMessageList.get(0);
//        ChatMessage latestMessage = chatMessageList.get(chatMessageList.size() - 1);
//        //then
//        assertThat(chatMessageList.size())
//                .isEqualTo(Long.valueOf(numberOfChatsToCreate - index - 1L).intValue());
//        //index 바로 다음부터
//        assertThat(chatMessageAtIndexPlusOne.getMessage())
//                .isEqualTo(lastMessageRetrieve.getMessage());
//        //가장 최근에 저장된 것까지 리턴
//        assertThat(latestMessage.getUserId())
//                .isEqualTo(numberOfChatsToCreate);
//    }
}
