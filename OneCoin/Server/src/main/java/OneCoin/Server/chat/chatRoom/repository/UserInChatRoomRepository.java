package OneCoin.Server.chat.chatRoom.repository;

import OneCoin.Server.chat.chatRoom.entity.UserInChatRoomInMemory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserInChatRoomRepository extends CrudRepository<UserInChatRoomInMemory, Long> {
    List<UserInChatRoomInMemory> findAllByChatRoomId(Long chatRoomId);

    List<UserInChatRoomInMemory> findAllByChatRoomIdAndUserId(Long chatRoomId, Long userId);

    List<UserInChatRoomInMemory> findAllByUserId(Long userId);
}
