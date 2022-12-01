package OneCoin.Server.chat.service;

import OneCoin.Server.chat.entity.ChatRoom;
import OneCoin.Server.chat.entity.UserInChatRoom;
import OneCoin.Server.chat.repository.ChatRoomRepository;
import OneCoin.Server.chat.repository.UserInChatRoomRepository;
import OneCoin.Server.chat.utils.ChatRoomUtils;
import OneCoin.Server.chat.repository.vo.UserInfoInChatRoom;
import OneCoin.Server.exception.BusinessLogicException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChatRoomServiceTest {
    @MockBean
    private ChatRoomRepository chatRoomRepository;
    @MockBean
    private UserInChatRoomRepository userInChatRoomRepository;
    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private ChatRoomUtils chatRoomUtils;
    private String sessionId;
    private String displayName;
    private String email;
    private UserInChatRoom user;
    private int chatRoomId1;
    private int chatRoomId2;
    private String roomId1Key;
    private String roomId2Key;
    private Set<String> keySet;


    @BeforeAll
    void stubData() {
        sessionId = "afds3d";
        displayName = "zoro";
        email = "zoro@naver.com";
        user = UserInChatRoom.builder().displayName(displayName).email(email).build();
        chatRoomId1 = 1;
        chatRoomId2 = 2;
        roomId1Key = chatRoomUtils.makeKey(chatRoomId1);
        roomId2Key = chatRoomUtils.makeKey(chatRoomId2);
        keySet = Sets.newSet(roomId1Key, roomId2Key);
    }
    @Test
    void findAllChatRoomsTest() {
        //given
        int roomId1 = 1;
        int roomId2 = 2;
        long numberOfChattersInRoom = 10L;
        Set<String> keySet = Sets.newSet(chatRoomUtils.makeKey(roomId1), chatRoomUtils.makeKey(roomId2));
        given(chatRoomRepository.findAll())
                .willReturn(keySet);
        given(userInChatRoomRepository.getNumberOfUserInChatRoom(any(Integer.class)))
                .willReturn(numberOfChattersInRoom);
        //when
        List<ChatRoom> chatRooms = chatRoomService.findAllChatRooms();
        //then
        assertThat(chatRooms.size()).isGreaterThanOrEqualTo(2);
        assertThat(chatRooms.get(0).getNumberOfChatters()).isEqualTo(numberOfChattersInRoom);
    }

    @Test
    void getNumberOfUserInChatRoomTest() {
        //given
        given(chatRoomRepository.contains(any(Integer.class)))
                .willReturn(false);
        int chatRoomId = 1;
        //when and then
        assertThatThrownBy(() -> {
            long num = chatRoomService.getNumberOfUserInChatRoom(chatRoomId);
        }).isInstanceOf(BusinessLogicException.class);
    }

    @Test
    void makeChatRoomTest_예외() {
        //given
        int roomId1 = 1;
        int roomId2 = 2;
        Set<String> keySet = Sets.newSet(chatRoomUtils.makeKey(roomId1), chatRoomUtils.makeKey(roomId2));
        given(chatRoomRepository.findAll())
                .willReturn(keySet);
        int roomIdToCreate = 4;
        //when and then
        assertThatThrownBy(() -> {
            chatRoomService.makeChatRoom(roomIdToCreate);
        }).isInstanceOf(BusinessLogicException.class);
    }

    @Test
    void makeChatRoomTest_정상() {
        //given
        int roomId1 = 1;
        int roomId2 = 2;
        Set<String> keySet = Sets.newSet(chatRoomUtils.makeKey(roomId1), chatRoomUtils.makeKey(roomId2));
        given(chatRoomRepository.findAll())
                .willReturn(keySet);
        int roomIdToCreate = 3;
        //when and then
        assertThatCode(() -> {
            chatRoomService.makeChatRoom(roomIdToCreate);
        }).doesNotThrowAnyException();
    }

    @Test
    void deleteUserFromChatRoomTest() {
        //given
        given(chatRoomRepository.findAll())
                .willReturn(keySet);
        given(userInChatRoomRepository.contain(eq(roomId1Key), any(String.class)))
                .willReturn(true);
        given(userInChatRoomRepository.contain(eq(roomId2Key), any(String.class)))
                .willReturn(false);
        given(userInChatRoomRepository.findBySessionId(any(String.class), any(String.class)))
                .willReturn(user);
        given(userInChatRoomRepository.removeUserBySessionId(any(String.class), any(String.class)))
                .willReturn(1L);
        //when
        UserInfoInChatRoom actual = chatRoomService.deleteUserFromChatRoom(sessionId);
        //then
        assertThat(actual.getUser().getDisplayName())
                .isEqualTo(displayName);
        assertThat(actual.getChatRoomId())
                .isEqualTo(chatRoomId1);
    }

    @Test
    void findUsersInChatRoomTest() {
        //given
        List<UserInChatRoom> users = List.of(user);
        given(userInChatRoomRepository.findAllByChatRoomId(any(Integer.class)))
                .willReturn(users);
        given(chatRoomRepository.contains(any(Integer.class)))
                .willReturn(true);
        //when
        List<UserInChatRoom> actual = chatRoomService.findUsersInChatRoom(chatRoomId1);
        //then
        assertThat(actual.size())
                .isEqualTo(1);
    }
}
