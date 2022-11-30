package OneCoin.Server.chat.repository;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChatRoomRepositoryTest {
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    private Integer chatRoomId1;
    private Integer chatRoomId2;
    private Integer numberOfChatRoomCreated;

    @BeforeAll
    void createChatRoom() {
        //채팅방 생성
        chatRoomId1 = 10;
        chatRoomId2 = 11;
        numberOfChatRoomCreated = 2;
        chatRoomRepository.create(chatRoomId1);
        chatRoomRepository.create(chatRoomId2);
    }
    @AfterAll
    void deleteChatRoom() {
        chatRoomRepository.delete(chatRoomId1);
        chatRoomRepository.delete(chatRoomId2);
    }
    @Test
    void findAllTest() {
        //when
        Set<String> chatRooms = chatRoomRepository.findAll();
        //then
        assertThat(chatRooms.size())
                .isGreaterThanOrEqualTo(numberOfChatRoomCreated);
    }

    @Test
    void containsTest_True() {
        //when
        boolean isExist = chatRoomRepository.contains(chatRoomId1);
        //then
        assertThat(isExist)
                .isTrue();
    }

    @Test
    void containsTest_False() {
        //given
        int chatRoomId = 10;
        //when
        boolean isExist = chatRoomRepository.contains(chatRoomId + 20);
        //then
        assertThat(isExist)
                .isFalse();
    }

    @Test
    void deleteChatRoomTest() {
        //given
        int chatRoomId = 200;
        chatRoomRepository.create(chatRoomId);
        int numberOfChatRoomsBefore = chatRoomRepository.findAll().size();
        //when
        chatRoomRepository.delete(chatRoomId);
        //then
        int numberOfChatRoomAfter = chatRoomRepository.findAll().size();
        assertThat(numberOfChatRoomsBefore - numberOfChatRoomAfter)
                .isEqualTo(1);
    }
}
