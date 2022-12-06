package OneCoin.Server.chat.repository;

import OneCoin.Server.chat.constant.MessageType;
import OneCoin.Server.chat.entity.ChatMessage;
import OneCoin.Server.chat.testUtil.WebSocketTestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        chatRoomId = 1020;
        for (long i = 1; i <= numberOfChatsToCreate; i++) {
            ChatMessage chatMessage = webSocketTestUtils.makeChatMessage(i, chatRoomId);
            chatMessageRdbRepository.save(chatMessage);
        }
    }
    @AfterAll
    void deleteAll() {
        chatMessageRdbRepository.deleteAll();
    }

    @Test
    void findTest() {
        List<ChatMessage> messages = chatMessageRdbRepository.findAll();
        assertThat(messages.size())
                .isEqualTo(numberOfChatsToCreate.intValue());
    }

    @Test
    void findAllByChatRoomIdTest() {
        List<ChatMessage> chatMessageList = chatMessageRdbRepository.findAllByChatRoomId(chatRoomId);
        assertThat(chatMessageList.size())
                .isEqualTo(numberOfChatsToCreate.intValue());
    }

    @Test
    void findByMessageAndChatAtAndUserId() {
        //given
        String message = "안녕";
        String chatAt = LocalDateTime.now().toString();
        Long userId = 1L;
        ChatMessage chatMessage = ChatMessage.builder()
                .message(message)
                .chatAt(chatAt)
                .chatRoomId(1)
                .userDisplayName("abc")
                .type(MessageType.TALK)
                .userId(userId)
                .build();
        chatMessageRdbRepository.save(chatMessage);
        //when
        Optional<ChatMessage> found = chatMessageRdbRepository.findByMessageAndChatAtAndUserId(
                message, chatAt, userId);
        //then
        assertThat(found.get().getMessage())
                .isEqualTo(message);
    }

    @Test
    void findTop10ByChatMessageIdLessThenEqualAndChatRoomIdTest() {
        //given
        List<ChatMessage> messagesa = chatMessageRdbRepository.findAll();
        //when
        List<ChatMessage> messages = chatMessageRdbRepository
                .findTop30ByChatRoomIdAndChatMessageIdLessThanEqualOrderByChatMessageIdDesc(chatRoomId, 15L);
        //then
        assertThat(messages.size())
                .isEqualTo(10);
        assertThat(messages.get(0).getChatMessageId())
                .isEqualTo(15L);

    }
}
