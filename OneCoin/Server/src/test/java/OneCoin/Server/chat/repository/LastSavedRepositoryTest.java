package OneCoin.Server.chat.repository;

import OneCoin.Server.chat.entity.ChatMessage;
import OneCoin.Server.chat.testUtil.WebSocketTestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class LastSavedRepositoryTest {
    @Autowired
    private LastSavedRepository lastSavedRepository;
    @Autowired
    private WebSocketTestUtils webSocketTestUtils;

    @Test
    void saveAndGetTest() {
        //given
        int chatRoomId = 200;
        Long userId = 500L;
        ChatMessage chatMessage = webSocketTestUtils.makeChatMessage(userId, chatRoomId);
        chatMessage.setChatMessageId(UUID.randomUUID().toString());

        ChatMessage chatMessage2 = webSocketTestUtils.makeChatMessage(userId + 20L, chatRoomId);
        chatMessage.setChatMessageId(UUID.randomUUID().toString());
        //when
        lastSavedRepository.save(chatRoomId, chatMessage);
        lastSavedRepository.save(chatRoomId, chatMessage2);
        ChatMessage chatMessageReceived = lastSavedRepository.get(chatRoomId);
        //then
        assertThat(chatMessageReceived.getUserId())
                .isEqualTo(userId + 20L);
    }
}
