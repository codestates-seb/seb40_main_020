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
        chatMessageRepository.deleteAll();
        for(long i = 1L; i <= 10L; i++) {
            chatMessageRepository.save(chatMessageMaker(i, 1L));
        }
    }

    @Test
    void test() {
        List<ChatMessage> chatMessageList = chatMessageRepository.findAllByChatRoomId(1L);
        assertThat(chatMessageList.size())
                .isEqualTo(10);
    }

    private ChatMessage chatMessageMaker(long userId, long chatRoomId) {
        return ChatMessage.builder()
                .chatRoomId(chatRoomId)
                .userDisplayName(String.valueOf(userId*100L))
                .message("hello")
                .userId(userId)
                .chatAt(LocalDateTime.now())
                .build();
    }
}
