package OneCoin.Server;

import OneCoin.Server.chat.chatRoom.entity.ChatRoom;
import OneCoin.Server.chat.chatRoom.repository.ChatRoomRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class ChatRoomRepositoryTest {
    @Autowired
    private ChatRoomRepository repository;

    @BeforeEach
    void chatRoomInit() {
        ChatRoom chatRoom1 = ChatRoom.builder().name("영차").nation("kr").build();
        ChatRoom chatRoom2 = ChatRoom.builder().name("영차영차").nation("kr").build();
        ChatRoom chatRoom3 = ChatRoom.builder().name("도지").nation("us").build();
        repository.save(chatRoom1);
        repository.save(chatRoom2);
    }

    @Test
    void 국가별조회() {
        //given
        String nation = "kr";
        //when
        List<ChatRoom> rooms = repository.findByNationOrderByChatRoomIdDesc(nation);
        //then
        assertThat(rooms.size())
                .isEqualTo(2);
    }
}
