package OneCoin.Server.chat.repository;

import OneCoin.Server.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRdbRepository extends JpaRepository<ChatMessage, String> {
    List<ChatMessage> findAllByChatRoomId(Integer chatRoomId);
}
