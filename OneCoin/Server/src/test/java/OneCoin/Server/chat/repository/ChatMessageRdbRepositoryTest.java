package OneCoin.Server.chat.repository;

import OneCoin.Server.chat.entity.ChatMessage;
import OneCoin.Server.chat.testUtil.WebSocketTestUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChatMessageRdbRepositoryTest {
    @Autowired
    private ChatMessageRdbRepository chatMessageRdbRepository;
    @Autowired
    private WebSocketTestUtils webSocketTestUtils;
    private Long numberOfChatsToCreate;
    private int chatRoomId;

    @BeforeAll
    void saveTest() {
        numberOfChatsToCreate = 120L;
        chatRoomId = 1000;
        for (long i = 1; i <= numberOfChatsToCreate; i++) {
            ChatMessage chatMessage = webSocketTestUtils.makeChatMessage(i, chatRoomId);
            chatMessage.setChatMessageId(UUID.randomUUID().toString());
            chatMessageRdbRepository.save(chatMessage);
        }
    }

    @Test
    void findTest() {
        List<ChatMessage> messages = chatMessageRdbRepository.findAll();
        assertThat(messages.size())
                .isEqualTo(numberOfChatsToCreate.intValue());
    }
}
