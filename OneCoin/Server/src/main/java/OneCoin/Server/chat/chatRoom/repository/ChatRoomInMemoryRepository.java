package OneCoin.Server.chat.chatRoom.repository;

import OneCoin.Server.chat.chatRoom.entity.ChatRoomInMemory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomInMemoryRepository extends CrudRepository<ChatRoomInMemory, Long> {
    List<ChatRoomInMemory> findAll();
}
