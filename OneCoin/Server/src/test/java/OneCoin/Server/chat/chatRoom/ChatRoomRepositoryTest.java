package OneCoin.Server.chat.chatRoom;

import OneCoin.Server.chat.chatRoom.entity.ChatRoom;
import OneCoin.Server.chat.chatRoom.repository.ChatRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class ChatRoomRepositoryTest {
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Test
    void saveTest() {
        //given
        String sessionId = "dfijf123";
        Integer chatRoomId = 1;

        //when
        chatRoomRepository.saveSessionIdToChatRoom(chatRoomId, sessionId);
        chatRoomRepository.saveSessionIdToChatRoom(chatRoomId, sessionId);

        //then
        long numberOfSessions = chatRoomRepository.getNumberOfSessionConnectedToChatRoom(chatRoomId);
        assertThat(numberOfSessions)
                .isEqualTo(1);

        chatRoomRepository.deleteSessionFromChatRoom(chatRoomId, sessionId);
    }

    @Test
    void saveTest_중복() {
        //given
        String sessionId1 = "aaaaaa";
        String sessionId2 = "bbbbbb";
        Integer chatRoomId = 1;

        //when
        chatRoomRepository.saveSessionIdToChatRoom(chatRoomId, sessionId1);
        chatRoomRepository.saveSessionIdToChatRoom(chatRoomId, sessionId1);
        chatRoomRepository.saveSessionIdToChatRoom(chatRoomId, sessionId2);

        //then
        long numberOfSessions = chatRoomRepository.getNumberOfSessionConnectedToChatRoom(chatRoomId);
        assertThat(numberOfSessions)
                .isEqualTo(2);

        chatRoomRepository.deleteSessionFromChatRoom(chatRoomId, sessionId1);
        chatRoomRepository.deleteSessionFromChatRoom(chatRoomId, sessionId2);
    }

    @Test
    void deleteTest() {
        //given
        String sessionId1 = "aaaaaa";
        Integer chatRoomId = 1;
        chatRoomRepository.saveSessionIdToChatRoom(chatRoomId, sessionId1);

        //when
        chatRoomRepository.deleteSessionFromChatRoom(chatRoomId, sessionId1);

        //then
        long numberOfSessions = chatRoomRepository.getNumberOfSessionConnectedToChatRoom(chatRoomId);
        assertThat(numberOfSessions)
                .isEqualTo(0);
    }

    @Test
    void makeChatRoomTest(){
        //given
        int chatRoomId = 1;
        //when
        chatRoomRepository.makeChatRoom(chatRoomId);
        //then
        boolean isExist = chatRoomRepository.doesChatRoomExist(chatRoomId);
        assertThat(isExist)
                .isTrue();

    }

    @Test
    void deleteChatRoomTest(){
        //given
        int chatRoomId = 1;
        //when
        chatRoomRepository.deleteChatRoom(chatRoomId);
        //then
        boolean isExist = chatRoomRepository.doesChatRoomExist(chatRoomId);
        assertThat(isExist)
                .isFalse();
    }

    @Test
    void findChatRoomsTest() {
        //given
        chatRoomRepository.makeChatRoom(1);
        chatRoomRepository.makeChatRoom(2);
        chatRoomRepository.saveSessionIdToChatRoom(1, "aaa");
        chatRoomRepository.saveSessionIdToChatRoom(1, "bbb");
        chatRoomRepository.saveSessionIdToChatRoom(2, "ccc");
        //when
        List<ChatRoom> chatRooms = chatRoomRepository.findChatRooms();
        //then
        assertThat(chatRooms.size()).isEqualTo(2);
        assertThat(chatRooms.get(0).getNumberOfChatters() +
                chatRooms.get(1).getNumberOfChatters())
                .isEqualTo(3);

        chatRoomRepository.deleteChatRoom(1);
        chatRoomRepository.deleteChatRoom(2);
    }

}
