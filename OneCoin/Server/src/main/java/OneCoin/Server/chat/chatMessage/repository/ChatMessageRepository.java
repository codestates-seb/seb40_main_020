package OneCoin.Server.chat.chatMessage.repository;

import OneCoin.Server.chat.chatMessage.entity.ChatMessage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends CrudRepository<ChatMessage, Long> {
    List<ChatMessage> findAllByChatRoomId(long chatRoomId);
//    @Query(value = "SELECT * FROM chatMessage c WHERE c.chatRoomId = :chatRoomId limit :limit")
//    List<ChatMessage> findAllByChatRoomIdTopN(@Param("chatRoomId") long chatRoomId, @Param("limit") int limit);
}
