package OneCoin.Server.chat;

import OneCoin.Server.chat.chatRoom.dto.ChatRoomDto;
import OneCoin.Server.chat.chatRoom.entity.ChatRoom;
import OneCoin.Server.chat.chatRoom.repository.ChatRoomRepository;
import OneCoin.Server.chat.chatRoom.service.ChatRoomService;
import OneCoin.Server.chat.fullTest.TestUtils;
import OneCoin.Server.config.auth.jwt.JwtTokenizer;
import OneCoin.Server.user.entity.User;
import OneCoin.Server.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
public class ChatRoomRepositoryTest {
    @Autowired
    private ChatRoomRepository repository;
    @Autowired
    private UserRepository userRepository;
    private TestUtils testUtils;
    @Autowired
    private JwtTokenizer jwtTokenizer;
    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private ChatRoomRepository chatRoomRepository;



    @BeforeEach
    void chatRoomInit() {
        testUtils = new TestUtils(jwtTokenizer);
        ChatRoom chatRoom1 = ChatRoom.builder().name("영차").nation("kr").build();
        ChatRoom chatRoom2 = ChatRoom.builder().name("영차영차").nation("kr").build();
        ChatRoom chatRoom3 = ChatRoom.builder().name("도지").nation("us").build();
        repository.save(chatRoom1);
        repository.save(chatRoom2);
        repository.save(chatRoom3);

        User user1 = userRepository.save(testUtils.makeUser("zoro"));
        User user2 = userRepository.save(testUtils.makeUser("nami"));
        User user3 = userRepository.save(testUtils.makeUser("loofy"));
        User user4 = userRepository.save(testUtils.makeUser("sangdi"));

        chatRoomService.registerUserToChatRoom(1L, user1);
        chatRoomService.registerUserToChatRoom(1L, user2);
        chatRoomService.registerUserToChatRoom(2L, user3);
        chatRoomService.registerUserToChatRoom(3L, user4);
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

    @Test
    void 이름조회() {
        //given
        String name = "도지";
        //when
        Optional<ChatRoom> room = repository.findByName(name);
        //then
        assertThat(room.isPresent())
                .isTrue();
    }

    @Test
    void 유저_수_계산() {
        //when
        List<ChatRoomDto> chatRoomDtos = chatRoomRepository.findAllRegisteredUserNumberGroupByChatRoom();
        //then
        ChatRoomDto actual = chatRoomDtos.stream()
                .filter(chatRoomDto -> chatRoomDto.getChatRoomId().equals(Long.valueOf(1L)))
                        .findAny().get();
        assertThat(actual.getNumberOfChatters())
                .isEqualTo(2);
    }
}
