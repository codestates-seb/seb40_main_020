package OneCoin.Server.chat;

import OneCoin.Server.chat.chatRoom.entity.ChatRoom;
import OneCoin.Server.chat.chatRoom.entity.ChatRoomUser;
import OneCoin.Server.chat.chatRoom.repository.ChatRoomRepository;
import OneCoin.Server.user.entity.Platform;
import OneCoin.Server.user.entity.Role;
import OneCoin.Server.user.entity.User;
import OneCoin.Server.user.repository.UserRepository;
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
    void test() {
        //given
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
        ChatRoom chatRoomSavedSaved = chatRoomRepository.save(chatRoom);
        //then
        assertThat(chatRoomSaved.getChatRoomUsers().stream().findFirst().get().getChatRoomUserId())
                .isNotNull();
    }
}
