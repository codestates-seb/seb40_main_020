package OneCoin.Server.chat.repository;

import OneCoin.Server.chat.entity.ChatMessage;
import OneCoin.Server.chat.testUtil.WebSocketTestUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChatMessageRepositoryTest {
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private LastSentScoreRepository lastSentScoreRepository;
    @Autowired
    private WebSocketTestUtils webSocketTestUtils;
    private Long numberOfChatsToCreate;
    private Integer chatRoomId;
    private String sessionId;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

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
    void findAllTest() {
        //when
        List<ChatMessage> chatRoomList = chatMessageRepository.findAll(chatRoomId);
        //then
        ChatMessage first = chatRoomList.get(0);
        assertThat(first.getUserId())
                .isEqualTo(numberOfChatsToCreate);
        assertThat(chatRoomList.size())
                .isEqualTo(numberOfChatsToCreate.intValue());
    }
}
