package OneCoin.Server.chat.service;

import OneCoin.Server.chat.constant.MessageType;
import OneCoin.Server.chat.entity.ChatMessage;
import OneCoin.Server.chat.entity.ChatRoom;
import OneCoin.Server.chat.repository.ChatMessageRdbRepository;
import OneCoin.Server.chat.repository.ChatMessageRepository;
import OneCoin.Server.chat.repository.LastSavedRepository;
import OneCoin.Server.chat.testUtil.WebSocketTestUtils;
import OneCoin.Server.config.auth.utils.UserUtilsForWebSocket;
import OneCoin.Server.user.entity.Platform;
import OneCoin.Server.user.entity.Role;
import OneCoin.Server.user.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import javax.annotation.PostConstruct;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChatServiceTest {
    @Mock
    private ChatMessageRepository chatMessageRepository;
    @Mock
    private UserUtilsForWebSocket userUtilsForWebSocket;
    @Mock
    private ChatMessageRdbRepository chatMessageRdbRepository;
    @Mock
    private LastSavedRepository lastSavedRepository;
    @Mock
    private ChatRoomService chatRoomService;
    @InjectMocks
    private ChatService chatService;
    private Integer chatRoomId;
    private User user;
    @BeforeAll
    void stubData() {
        chatRoomId = 1;
        user = User.builder()
                .userId(1L).userRole(Role.ROLE_USER).displayName("zoro").email("zoro@naver.com")
                .platform(Platform.KAKAO).password("1234@1fsvjf").build();
    }

    @Test
    void makeEnterOrLeaveChatMessage_테스트_ENTER인_경우() {
        //given
        MessageType messageType = MessageType.ENTER;
        //when
        ChatMessage actual = chatService.makeEnterOrLeaveChatMessage(messageType, chatRoomId, user);
        //
        assertThat(actual.getMessage())
                .contains("입장");
    }

    @Test
    void makeEnterOrLeaveChatMessage_테스트_LEAVE인_경우() {
        //given
        MessageType messageType = MessageType.LEAVE;
        //when
        ChatMessage actual = chatService.makeEnterOrLeaveChatMessage(messageType, chatRoomId, user);
        //
        assertThat(actual.getMessage())
                .contains("퇴장");
    }

    @Test
    void setInfoAndSaveMessage_테스트() {
        //given
        ChatMessage chatMessage = ChatMessage.builder()
                .message("hello")
                .chatRoomId(chatRoomId)
                .build();
        String displayName = "zoro";
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", Integer.valueOf(1));
        claims.put("displayName", displayName);
        Authentication authentication = new UsernamePasswordAuthenticationToken(claims, null);
        given(userUtilsForWebSocket.extractClaims(any(Principal.class)))
                .willReturn(claims);

        //when
        ChatMessage actual = chatService.setInfoAndSaveMessage(chatMessage, authentication);
        //then
        assertThat(actual.getType())
                .isEqualTo(MessageType.TALK);
        assertThat(actual.getUserDisplayName())
                .isEqualTo(displayName);
    }

    @DisplayName("최초에 저장하는 경우")
    @Test
    void saveInMemoryChatMessagesToRdbTest_최초() {
        ChatRoom chatRoom1 = ChatRoom.builder().chatRoomId(1).numberOfChatters(10L).build();
        ChatRoom chatRoom2 = ChatRoom.builder().chatRoomId(2).numberOfChatters(10L).build();
        List<ChatRoom> chatRooms = List.of(chatRoom1, chatRoom2);
        ChatMessage chatMessage1 = ChatMessage.builder().message("1").build();
        ChatMessage chatMessage2 = ChatMessage.builder().message("2").build();
        ChatMessage chatMessage3 = ChatMessage.builder().message("3").build();
        List<ChatMessage> chatMessages = List.of(chatMessage1, chatMessage2, chatMessage3);
        given(chatRoomService.findAllChatRooms())
                .willReturn(chatRooms);
        given(lastSavedRepository.get(any(Integer.class)))
                .willReturn(null);
        given(chatMessageRepository.findAll(any(Integer.class)))
                .willReturn(chatMessages);
        assertThatNoException().isThrownBy(() -> {
            chatService.saveInMemoryChatMessagesToRdb();
        });
    }

    @DisplayName("이후에 저장하는 경우")
    @Test
    void saveInMemoryChatMessagesToRdbTest_이후() {
        ChatRoom chatRoom1 = ChatRoom.builder().chatRoomId(1).numberOfChatters(10L).build();
        ChatRoom chatRoom2 = ChatRoom.builder().chatRoomId(2).numberOfChatters(10L).build();
        List<ChatRoom> chatRooms = List.of(chatRoom1, chatRoom2);
        ChatMessage chatMessage1 = ChatMessage.builder().message("1").build();
        ChatMessage chatMessage2 = ChatMessage.builder().message("2").build();
        ChatMessage chatMessage3 = ChatMessage.builder().message("3").build();
        List<ChatMessage> chatMessages = List.of(chatMessage1, chatMessage2, chatMessage3);
        given(chatRoomService.findAllChatRooms())
                .willReturn(chatRooms);
        given(lastSavedRepository.get(any(Integer.class)))
                .willReturn(chatMessage1);
        given(chatMessageRepository.findAllAfter(any(Integer.class), any(Long.class)))
                .willReturn(chatMessages);
        assertThatNoException().isThrownBy(() -> {
            chatService.saveInMemoryChatMessagesToRdb();
        });
    }

}
