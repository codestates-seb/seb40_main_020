package OneCoin.Server.chat.chatRoom.repository;

import OneCoin.Server.chat.chatRoom.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findByNationOrderByChatRoomIdDesc(String nation);

    Optional<ChatRoom> findByName(String name);
}
