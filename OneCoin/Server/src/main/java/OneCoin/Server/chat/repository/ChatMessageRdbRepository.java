package OneCoin.Server.chat.repository;

import OneCoin.Server.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRdbRepository extends JpaRepository<ChatMessage, String> {
}
