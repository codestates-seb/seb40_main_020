package OneCoin.Server.chat.chatRoom.repository;

import OneCoin.Server.chat.chatRoom.dto.ChatRoomDto;
import OneCoin.Server.chat.chatRoom.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findByNationOrderByChatRoomIdDesc(String nation);
    Optional<ChatRoom> findByName(String name);

    @Query("SELECT " +
            "    new OneCoin.Server.chat.chatRoom.dto.ChatRoomDto" +
                "(cr.chatRoomId, cr.name, cr.nation, COUNT(cru)) " +
            "FROM " +
            "    ChatRoom cr " +
            "LEFT OUTER JOIN" +
            "    cr.chatRoomUsers cru " +
            "GROUP BY " +
            "    cr.chatRoomId")
    List<ChatRoomDto> findAllRegisteredUserNumberGroupByChatRoom();

}
