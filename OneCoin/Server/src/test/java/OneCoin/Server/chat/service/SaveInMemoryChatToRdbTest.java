package OneCoin.Server.chat.service;

import OneCoin.Server.chat.entity.ChatMessage;
import OneCoin.Server.chat.repository.ChatMessageRdbRepository;
import OneCoin.Server.chat.repository.ChatMessageRepository;
import OneCoin.Server.chat.repository.ChatRoomRepository;
import OneCoin.Server.chat.repository.LastSavedRepository;
import OneCoin.Server.chat.testUtil.WebSocketTestUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SaveInMemoryChatToRdbTest {
    @Autowired
    private ChatService chatService;
    private Long numberOfChatsToCreate1;
    private Integer chatRoomId1;
    private Long numberOfChatsToCreate2;
    private Integer chatRoomId2;
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private WebSocketTestUtils webSocketTestUtils;
    @Autowired
    private ChatMessageRdbRepository chatMessageRdbRepository;
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private LastSavedRepository lastSavedRepository;

    @BeforeAll
    void init() {
        numberOfChatsToCreate1 = 120L;
        chatRoomId1 = 1;
        numberOfChatsToCreate2 = 200L;
        chatRoomId2 = 2;

        chatRoomRepository.delete(chatRoomId1);
        chatRoomRepository.delete(chatRoomId2);
        chatRoomRepository.create(chatRoomId1);
        chatRoomRepository.create(chatRoomId2);

        lastSavedRepository.delete(chatRoomId1);
        lastSavedRepository.delete(chatRoomId2);

        chatMessageRepository.removeAllInChatRoom(chatRoomId1);
        chatMessageRepository.removeAllInChatRoom(chatRoomId2);

        saveMessages(1L, numberOfChatsToCreate1);
    }

    @Test
    @DisplayName("처음 저장하는 경우")
    void saveTest() {
        chatService.saveInMemoryChatMessagesToRdb();
        List<ChatMessage> chatMessageList = chatMessageRdbRepository.findAll();
        assertThat(chatMessageList.size())
                .isEqualTo(numberOfChatsToCreate1 * 2);
    }

    @Test
    @DisplayName("이후에 저장하는 경우")
    void saveTest_최초이후() {
        //given
        saveMessages(numberOfChatsToCreate1 + 1L, numberOfChatsToCreate2);
        //when
        chatService.saveInMemoryChatMessagesToRdb();
        //then
        List<ChatMessage> chatMessageList = chatMessageRdbRepository.findAllByChatRoomId(chatRoomId1);
        assertThat(chatMessageList.size())
                .isEqualTo(numberOfChatsToCreate2);
    }

    private void saveMessages(long from, long to) {
        for (long i = from; i <= to; i++) {
            chatMessageRepository.save(webSocketTestUtils.makeChatMessage(i, chatRoomId1));
        }
        for (long i = from; i <= to; i++) {
            chatMessageRepository.save(webSocketTestUtils.makeChatMessage(i, chatRoomId2));
        }
    }
}
