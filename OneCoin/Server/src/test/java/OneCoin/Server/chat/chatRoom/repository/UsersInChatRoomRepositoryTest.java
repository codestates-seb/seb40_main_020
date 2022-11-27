package OneCoin.Server.chat.chatRoom.repository;

import OneCoin.Server.chat.chatRoom.entity.UserInChatRoom;
import OneCoin.Server.chat.chatRoom.utils.ChatRoomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class UsersInChatRoomRepositoryTest {
    @Autowired
    private UserInChatRoomRepository repository;
    @Autowired
    private ChatRoomUtils chatRoomUtils;


    @BeforeEach
    void clearRepo() {
        repository.removeAllInChatRoom(1);
    }

    @Test
    void addUserTest_비회원_중복() {
        //given
        Integer chatRoomId = 1;
        String sessionId = "cccccc";
        UserInChatRoom user = null;

        //when
        repository.addUser(chatRoomId, sessionId, null);
        repository.addUser(chatRoomId, sessionId, null);

        //then
        long numberOfSessions = repository.getNumberOfUserInChatRoom(chatRoomId);
        assertThat(numberOfSessions)
                .isEqualTo(1);

        repository.removeUserBySessionId(chatRoomUtils.getKey(chatRoomId), sessionId);
    }

    @Test
    void saveTest_회원_중복() {
        //given
        Integer chatRoomId = 1;
        String sessionId1 = "aaaaaa";
        String sessionId2 = "bbbbbb";
        UserInChatRoom user = UserInChatRoom.builder().displayName("zoro").email("zoro@naver.com").build();

        //when
        repository.addUser(chatRoomId, sessionId1, user);
        repository.addUser(chatRoomId, sessionId1, user);
        repository.addUser(chatRoomId, sessionId2, user);

        //then
        long numberOfSessions = repository.getNumberOfUserInChatRoom(chatRoomId);
        assertThat(numberOfSessions)
                .isEqualTo(2);

        repository.removeUserBySessionId(chatRoomUtils.getKey(chatRoomId), sessionId1);
        repository.removeUserBySessionId(chatRoomUtils.getKey(chatRoomId), sessionId2);
    }

    @Test
    void removeUserByIdTest() {
        //given
        String sessionId1 = "aaaaaa";
        Integer chatRoomId = 1;
        repository.addUser(chatRoomId, sessionId1, null);

        //when
        repository.removeUserBySessionId(chatRoomUtils.getKey(chatRoomId), sessionId1);

        //then
        long numberOfSessions = repository.getNumberOfUserInChatRoom(chatRoomId);
        assertThat(numberOfSessions)
                .isEqualTo(0);
    }

    @Test
    void findAllByChatRoomIdTest() {
        //given
        String sessionId1 = "aaaaaa";
        Integer chatRoomId = 1;
        UserInChatRoom user = UserInChatRoom.builder()
                .displayName("fkfkf")
                .email("sdfsdf")
                .build();
        repository.addUser(chatRoomId, sessionId1, user);

        //when
        List<UserInChatRoom> users = repository.findAllByChatRoomId(chatRoomId);

        //then
        assertThat(users.size())
                .isEqualTo(1);
    }

    @Test
    void findBySessionIdTest() {
        String sessionId1 = "aaaaaa";
        Integer chatRoomId = 1;
        UserInChatRoom user = UserInChatRoom.builder()
                .displayName("fkfkf")
                .email("sdfsdf")
                .build();
        repository.addUser(chatRoomId, sessionId1, user);
        UserInChatRoom userInChatRoom = repository.findBySessionId(chatRoomUtils.getKey(chatRoomId), sessionId1);
        assertThat(userInChatRoom.getDisplayName())
                .isEqualTo("fkfkf");
    }
}
