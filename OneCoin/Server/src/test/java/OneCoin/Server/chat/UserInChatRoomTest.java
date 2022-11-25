package OneCoin.Server.chat;

import OneCoin.Server.chat.chatRoom.entity.UserInChatRoomInMemory;
import OneCoin.Server.chat.chatRoom.repository.UserInChatRoomRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class UserInChatRoomTest {
    @Autowired
    private UserInChatRoomRepository repository;
    @Test
    void test() {
        //given
        UserInChatRoomInMemory user = UserInChatRoomInMemory.builder()
                .userId(1L)
                .chatRoomId(2L)
                .userDisplayName("zoro")
                .build();
        //when
        repository.deleteAll();
        UserInChatRoomInMemory savedUser = repository.save(user);
        List<UserInChatRoomInMemory> foundUsers = repository.findAllByChatRoomIdAndUserId(2L, 1L);
        repository.deleteAll(foundUsers);
//        repository.deleteAllByChatRoomIdAndUserId(2L, 1L);
        //then
        Optional<UserInChatRoomInMemory> optional = repository.findById(savedUser.getId());
        assertThat(optional.isEmpty())
                .isTrue();
    }
}
