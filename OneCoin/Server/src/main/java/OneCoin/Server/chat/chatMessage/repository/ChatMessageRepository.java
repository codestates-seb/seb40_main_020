package OneCoin.Server.chat.chatMessage.repository;

import OneCoin.Server.chat.chatMessage.entity.ChatMessage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends CrudRepository<ChatMessage, Long> {
}
