package OneCoin.Server.chat.repository;

import OneCoin.Server.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageRdbRepository extends JpaRepository<ChatMessage, String> {
    List<ChatMessage> findAllByChatRoomId(Integer chatRoomId);

    Optional<ChatMessage> findByMessageAndChatAtAndUserId(String message, String chatAt, Long userId);
    List<ChatMessage> findTop30ByChatRoomIdAndChatMessageIdLessThanEqualOrderByChatMessageIdDesc(Integer chatRoomId, Long chatMessageId);
}
