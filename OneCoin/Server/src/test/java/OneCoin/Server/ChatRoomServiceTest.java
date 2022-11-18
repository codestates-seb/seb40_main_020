package OneCoin.Server;

import OneCoin.Server.chat.chatRoom.entity.ChatRoom;
import OneCoin.Server.chat.chatRoom.repository.ChatRoomRepository;
import OneCoin.Server.chat.chatRoom.service.ChatRoomService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ChatRoomServiceTest {
    @Mock
    private ChatRoomRepository chatRoomRepository;
    @InjectMocks
    private ChatRoomService chatRoomService;

    @Test
    public void findChatRoomTest_없는_ID_경우() {
        //given
        long roomId = 2L;
        given(chatRoomRepository.findById(roomId))
                .willReturn(Optional.empty());
        //when
        Throwable throwable = catchThrowable(() -> {
            ChatRoom createdRoom = chatRoomService.findChatRoom(roomId);
        });
        //then
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void findChatRoomTest_있는_경우_경우() {
        //given
        long roomId = 1L;
        ChatRoom chatRoom = ChatRoom.builder().name("도지").nation("kr").build();
        chatRoom.setChatRoomId(1L);
        given(chatRoomRepository.findById(roomId))
                .willReturn(Optional.of(chatRoom));
        //when
        ChatRoom createdRoom = chatRoomService.findChatRoom(roomId);
        //then
        assertThat(createdRoom.getChatRoomId())
                .isEqualTo(roomId);
    }

    @Test
    void findRoomByNationTest_국가가_없는_경우() {
        //given
        String nation = "uk";
        //when
        Throwable throwable = catchThrowable(() -> {
            List<ChatRoom> createdRooms = chatRoomService.findRoomByNation(nation);
        });
        //then
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findRoomByNationTest_정상_케이스() {
        //given
        String nation = "kr";
        ChatRoom chatRoom = ChatRoom.builder().name("도지").nation("kr").build();
        chatRoom.setChatRoomId(1L);
        given(chatRoomRepository.findByNationOrderByChatRoomIdDesc(nation))
                .willReturn(List.of(chatRoom));
        //when
        List<ChatRoom> createdRooms = chatRoomService.findRoomByNation(nation);

        //then
        assertThat(createdRooms.get(0).getName())
                .isEqualTo("도지");
    }

    @Test
    void createRoomTest_이름이_중복되는_케이스() {
        //given
        String nation = "kr";
        String name = "도지";
        ChatRoom chatRoom = ChatRoom.builder().name(name).nation(nation).build();
        chatRoom.setChatRoomId(1L);
        given(chatRoomRepository.findByName(name))
                .willReturn(Optional.of(chatRoom));
        //when
        Throwable throwable = catchThrowable(() -> {
            ChatRoom createdRooms = chatRoomService.createRoom(name, nation);
        });
        //then
        assertThat(throwable)
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void createRoomTest_정상_케이스() {
        //given
        String nation = "kr";
        String name = "도지";
        ChatRoom chatRoom = ChatRoom.builder().name(name).nation(nation).build();
        chatRoom.setChatRoomId(1L);
        given(chatRoomRepository.findByName(Mockito.any(String.class)))
                .willReturn(Optional.empty());
        given(chatRoomRepository.save(Mockito.any(ChatRoom.class)))
                .willReturn(chatRoom);
        //when
        ChatRoom createdRooms = chatRoomService.createRoom(name, nation);
        //then
        assertThat(createdRooms.getName())
                .isEqualTo(name);
    }
}
