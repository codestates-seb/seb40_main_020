package OneCoin.Server.chat.repository;

import OneCoin.Server.chat.entity.ChatMessage;
import OneCoin.Server.chat.constant.MessageType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class ChatMessageRepositoryTest {
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @BeforeEach
    void init() {
        for (long i = 60L; i <= 70L; i++) {
            chatMessageRepository.save(chatMessageMaker(i, 20));
        }
    }

    @Test
    void getMessageFromRoomLimitNTest() {
        List<ChatMessage> chatMessageList = chatMessageRepository.getMessageFromRoomLimitN(20, 10L);
        assertThat(chatMessageList.size())
                .isEqualTo(10);
        assertThat(chatMessageList.get(0).getUserId()
                .equals(Long.valueOf(70L)));
    }

    private ChatMessage chatMessageMaker(long userId, Integer chatRoomId) {
        return new ChatMessage(
                MessageType.TALK,
                "hello" + userId,
                LocalDateTime.now().toString(),
                userId,
                chatRoomId,
                String.valueOf(userId * 100L));
    }
}
