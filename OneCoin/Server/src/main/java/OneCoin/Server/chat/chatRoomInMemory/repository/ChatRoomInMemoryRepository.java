package OneCoin.Server.chat.chatRoomInMemory.repository;

import OneCoin.Server.chat.chatRoomInMemory.entity.ChatRoomInMemory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomInMemoryRepository extends CrudRepository<ChatRoomInMemory, Long> {
    List<ChatRoomInMemory> findAll();
}
