package OneCoin.Server.chat;

import OneCoin.Server.chat.chatMessage.entity.ChatMessage;
import OneCoin.Server.chat.chatMessage.repository.ChatMessageRepository;
import org.aspectj.lang.annotation.Before;
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
        for(long i = 60L; i <= 70L; i++) {
            chatMessageRepository.save(chatMessageMaker(i, 20L));
        }
    }

    @Test
    void test() {
        List<ChatMessage> chatMessageList = chatMessageRepository.getMessageFromRoomLimitN(20L, 10);
        assertThat(chatMessageList.size())
                .isEqualTo(10);
        assertThat(chatMessageList.get(0).getUserId()
                .equals(Long.valueOf(70L)));
    }

    private ChatMessage chatMessageMaker(long userId, long chatRoomId) {
        return new ChatMessage(
                "hello" + userId,
                LocalDateTime.now().toString(),
                userId,
                chatRoomId,
                String.valueOf(userId*100L));
    }
}
