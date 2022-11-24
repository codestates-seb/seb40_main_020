package OneCoin.Server.chat.chatRoom.repository;

import OneCoin.Server.chat.chatRoom.entity.ChatRoomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomUserRepository extends JpaRepository<ChatRoomUser, Long> {
}
