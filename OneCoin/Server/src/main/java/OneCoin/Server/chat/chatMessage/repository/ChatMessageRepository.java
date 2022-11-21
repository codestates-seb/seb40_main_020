package OneCoin.Server.chat.chatMessage.repository;

import OneCoin.Server.chat.chatMessage.entity.ChatMessage;
import org.springframework.data.repository.CrudRepository;

public interface ChatMessageRepository extends CrudRepository<ChatMessage, Long> {
}
