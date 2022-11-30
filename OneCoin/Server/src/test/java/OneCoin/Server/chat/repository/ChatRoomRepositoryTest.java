package OneCoin.Server.chat.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class ChatRoomRepositoryTest {
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Test
    void createAndFindAllTest() {
        //givn
        Integer chatRoomId1 = 1;
        Integer chatRoomId2 = 2;
        //when
        chatRoomRepository.create(chatRoomId1);
        chatRoomRepository.create(chatRoomId2);
        //then
        Set<String> chatRooms = chatRoomRepository.findAll();
        assertThat(chatRooms.size())
                .isEqualTo(2);

    }

    @Test
    void containsTest_True() {
        //given
        int chatRoomId = 1;
        //when
        boolean isExist = chatRoomRepository.contains(chatRoomId);
        //then
        assertThat(isExist)
                .isTrue();
    }

    @Test
    void containsTest_False() {
        //given
        int chatRoomId = 10;
        //when
        boolean isExist = chatRoomRepository.contains(chatRoomId);
        //then
        assertThat(isExist)
                .isFalse();
    }

    @Test
    void deleteChatRoomTest() {
        //given
        int chatRoomIdToDelete = 1;
        //when
        chatRoomRepository.delete(chatRoomIdToDelete);
        //then
        Set<String> chatRooms = chatRoomRepository.findAll();
        assertThat(chatRooms.size())
                .isEqualTo(1);
    }
}
