package OneCoin.Server.chat.repository;

import OneCoin.Server.chat.entity.ChatMessage;
import OneCoin.Server.chat.testUtil.WebSocketTestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChatMessageRepositoryTest {
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private WebSocketTestUtils webSocketTestUtils;
    private Long numberOfChatsToCreate;
    private Integer chatRoomId;
    @Autowired
    private RedisTemplate<String, ChatMessage> redisTemplate;
    // <chatRoomKey, ChatMessage>
    private ListOperations<String, ChatMessage> operations;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void saveMessages() {
        operations = redisTemplate.opsForList();
        numberOfChatsToCreate = 120L;
        chatRoomId = 1000;
        for (long i = 1; i <= numberOfChatsToCreate; i++) {
            chatMessageRepository.save(webSocketTestUtils.makeChatMessage(i, chatRoomId));
        }
    }

    @AfterEach
    void deleteMessages() {
        chatMessageRepository.removeAllInChatRoom(chatRoomId);
    }

    @Test
    void getMessageFromRoomTest() {
        //given
        long limit = chatMessageRepository.NUMBER_OF_CHATS_TO_SHOW;
        //when
        List<ChatMessage> chatMessageList = chatMessageRepository.getMessageFromRoom(chatRoomId);
        //then
        assertThat(chatMessageList.size())
                .isEqualTo(limit);
        assertThat(chatMessageList.get(0).getUserId())
                .isEqualTo(numberOfChatsToCreate);
    }

    @Test
    void findAllTest() {
        //when
        List<ChatMessage> chatRoomList = chatMessageRepository.findAll(chatRoomId);
        //then
        assertThat(chatRoomList.size())
                .isEqualTo(numberOfChatsToCreate.intValue());
    }

    @Test
    void getIndexTest() {
        //given
        List<ChatMessage> chatMessageList = chatMessageRepository.getMessageFromRoom(chatRoomId);
        ChatMessage latest = chatMessageList.get(0);
        long numberOfChatsToCreate = 10;
        for (long i = 1; i <= numberOfChatsToCreate; i++) {
            chatMessageRepository.save(webSocketTestUtils.makeChatMessage(i, chatRoomId));
        }

        //when
        Long index = chatMessageRepository.getIndex(chatRoomId, latest);

        //then
        assertThat(index)
                .isEqualTo(numberOfChatsToCreate);
    }

    @Test
    void findAllAfter() {
        //given
        Long index = 10L;
        //when
        List<ChatMessage> chatMessageList = chatMessageRepository.findAllAfter(chatRoomId, index);
        //then
        assertThat(chatMessageList.size())
                .isEqualTo(11);
    }

}
