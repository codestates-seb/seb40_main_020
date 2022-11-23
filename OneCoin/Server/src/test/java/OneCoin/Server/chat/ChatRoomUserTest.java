package OneCoin.Server.chat;

import OneCoin.Server.chat.chatRoom.entity.ChatRoom;
import OneCoin.Server.chat.chatRoom.entity.ChatRoomUser;
import OneCoin.Server.chat.chatRoom.repository.ChatRoomRepository;
import OneCoin.Server.user.entity.Platform;
import OneCoin.Server.user.entity.Role;
import OneCoin.Server.user.entity.User;
import OneCoin.Server.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
public class ChatRoomUserTest {
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @BeforeEach
    void init() {
        ChatRoom chatRoom = ChatRoom.builder()
                .name("도지코인")
                .nation("kr")
                .build();
        ChatRoom chatRoomSaved = chatRoomRepository.save(chatRoom);
        User user = User.builder()
                .userRole(Role.ROLE_USER)
                .balance(0L)
                .displayName("zoro")
                .email("zoro@naver.com")
                .password("1234")
                .platform(Platform.KAKAO)
                .build();
        User userSaved = userRepository.save(user);
        ChatRoomUser chatRoomUser = ChatRoomUser.builder()
                .user(userSaved)
                .chatRoom(chatRoomSaved)
                .build();
        chatRoomSaved.addChatRoomUser(chatRoomUser);
        //when
        chatRoomRepository.save(chatRoom);
    }

    @Test
    void hashCodeAndEquals_재정의_테스트() {
        ChatRoom chatRoom = ChatRoom.builder()
                .chatRoomId(1L)
                .name("도지코인")
                .nation("kr")
                .build();
        User user = User.builder()
                .userId(1L)
                .userRole(Role.ROLE_USER)
                .balance(0L)
                .displayName("zoro")
                .email("zoro@naver.com")
                .password("1234")
                .platform(Platform.KAKAO)
                .build();
        ChatRoomUser obj1 = ChatRoomUser.builder()
                .chatRoom(chatRoom)
                .user(user)
                .build();
        ChatRoomUser obj2 = ChatRoomUser.builder()
                .chatRoom(chatRoom)
                .user(user)
                .build();
        assertThat(obj1.equals(obj2))
                .isTrue();
    }

    @Test
    void hashCodeAndEquals_재정의_테스트2() {
        ChatRoom chatRoom = chatRoomRepository.findById(1L).get();

        ChatRoomUser chatRoomUser = ChatRoomUser.builder()
                .user(User.builder().userId(1L).build())
                .chatRoom(ChatRoom.builder().chatRoomId(1L).build())
                .build();
        boolean isContained = chatRoom.getChatRoomUsers().contains(chatRoomUser);
        assertThat(isContained)
                .isTrue();
    }

    @Test
    void hashCodeAndEquals_재정의_테스트3() {
        ChatRoom chatRoom = chatRoomRepository.findById(1L).get();

        ChatRoomUser chatRoomUser = ChatRoomUser.builder()
                .user(User.builder().userId(1L).build())
                .chatRoom(ChatRoom.builder().chatRoomId(1L).build())
                .build();
        boolean isContained = chatRoom.getChatRoomUsers().remove(chatRoomUser);
        int size = chatRoom.getChatRoomUsers().size();
        assertThat(isContained)
                .isTrue();
        assertThat(size)
                .isEqualTo(0);
    }
    @Test
    void hashCodeAndEquals_재정의_테스트4() {
        ChatRoom chatRoom = chatRoomRepository.findById(1L).get();

        ChatRoomUser chatRoomUser = ChatRoomUser.builder()
                .user(User.builder().userId(2L).build())
                .chatRoom(ChatRoom.builder().chatRoomId(1L).build())
                .build();
        boolean isContained = chatRoom.getChatRoomUsers().remove(chatRoomUser);
        int size = chatRoom.getChatRoomUsers().size();
        assertThat(isContained)
                .isFalse();
        assertThat(size)
                .isEqualTo(1);
    }
}
