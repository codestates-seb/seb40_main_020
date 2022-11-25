package OneCoin.Server.chat.chatRoomInMemory.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@Setter
@RedisHash(value = "usersInChatRoomInMemory", timeToLive = -1L)
@Builder
public class UserInChatRoomInMemory {
    @Id
    private Long id;
    @Indexed
    private Long chatRoomId;
    @Indexed
    private Long userId;
    private String userDisplayName;
    private String userEmail;
}
