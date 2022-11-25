package OneCoin.Server.chat.chatRoom.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;



@Getter
@Setter
@RedisHash(value = "chatRoomInMemory", timeToLive = -1L)
@Builder
public class ChatRoomInMemory {
    @Id
    private Long chatRoomId;
    private long numberOfChatters = 0L;

    public void increaseNumberOfChatters() {
        numberOfChatters++;
    }

    public void decreaseNumberOfChatters() {
        numberOfChatters--;
    }
}
