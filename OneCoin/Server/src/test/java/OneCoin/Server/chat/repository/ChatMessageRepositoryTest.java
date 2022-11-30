package OneCoin.Server.chat.repository;

import OneCoin.Server.chat.constant.MessageType;
import OneCoin.Server.chat.entity.ChatMessage;
import OneCoin.Server.chat.testUtil.TestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChatMessageRepositoryTest {
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private TestUtils testUtils;
    private Long startChatRoomId;
    private Long endChatRoomId;
    private Integer chatRoomIdSavedTo;

    @BeforeAll
    void saveMessages() {
        startChatRoomId = 60L;
        endChatRoomId = 69L;
        chatRoomIdSavedTo = 1000;
        for (long i = startChatRoomId; i <= endChatRoomId; i++) {
            chatMessageRepository.save(testUtils.chatMessageMaker(i, chatRoomIdSavedTo));
        }
    }

    @AfterAll
    void deleteMessages() {
        chatMessageRepository.removeAllInChatRoom(chatRoomIdSavedTo);
    }

    @Test
    void getMessageFromRoomLimitNTest() {
        //given
        long limit = 5L;
        //when
        List<ChatMessage> chatMessageList = chatMessageRepository.getMessageFromRoomLimitN(chatRoomIdSavedTo, limit);
        //then
        assertThat(chatMessageList.size())
                .isEqualTo(limit);
    }
}
