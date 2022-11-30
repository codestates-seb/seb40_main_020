package OneCoin.Server.chat.repository;

import OneCoin.Server.chat.entity.UserInChatRoom;
import OneCoin.Server.chat.utils.ChatRoomUtils;
import org.junit.jupiter.api.*;
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
    private Integer chatRoomId;
    private String 비회원SessionId;
    private String 회원SessionId;
    private UserInChatRoom 회원;
    private int totalNumberOfChatters = 2;



    @BeforeEach
    void saveMembers() {
        // 비회원
        chatRoomId = 1999;
        비회원SessionId = "cccccc";

        repository.addUser(chatRoomId, 비회원SessionId, null);

        //회원
        회원SessionId = "ddddd";
        회원 = UserInChatRoom.builder().email("zoro@naver.com").displayName("zoro").build();

        repository.addUser(chatRoomId, 회원SessionId, 회원);
    }

    @AfterEach
    void clearRepo() {
        repository.removeAllInChatRoom(chatRoomId);
    }

    @Test
    void addUserTest_비회원_중복() {
        //when
        repository.addUser(chatRoomId, 비회원SessionId, null);

        //then
        long numberOfSessions = repository.getNumberOfUserInChatRoom(chatRoomId);
        assertThat(numberOfSessions)
                .isEqualTo(totalNumberOfChatters);
    }

    @Test
    void saveTest_회원_중복() {
        //when
        repository.addUser(chatRoomId, 비회원SessionId, 회원);
        repository.addUser(chatRoomId, 비회원SessionId, 회원);
        repository.addUser(chatRoomId, 회원SessionId, 회원);

        //then
        long numberOfSessions = repository.getNumberOfUserInChatRoom(chatRoomId);
        assertThat(numberOfSessions)
                .isEqualTo(totalNumberOfChatters);
    }

    @Test
    void removeUserByIdTest() {
        //when
        repository.removeUserBySessionId(chatRoomUtils.makeKey(chatRoomId), 비회원SessionId);

        //then
        long numberOfSessions = repository.getNumberOfUserInChatRoom(chatRoomId);
        assertThat(numberOfSessions)
                .isEqualTo(totalNumberOfChatters - 1);
    }

    @Test
    void findAllByChatRoomIdTest() {
        //when
        List<UserInChatRoom> users = repository.findAllByChatRoomId(chatRoomId);

        //then
        assertThat(users.size())
                .isEqualTo(totalNumberOfChatters);
    }

    @Test
    void findBySessionIdTest() {
        //when
        UserInChatRoom userInChatRoom = repository.findBySessionId(chatRoomUtils.makeKey(chatRoomId), 회원SessionId);
        assertThat(userInChatRoom.getDisplayName())
                .isEqualTo(회원.getDisplayName());
    }
}
