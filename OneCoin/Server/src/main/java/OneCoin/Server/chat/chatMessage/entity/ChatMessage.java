package OneCoin.Server.chat.chatMessage.entity;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Getter
@Setter
@RedisHash(value = "chatMessage", timeToLive = -1L)
@Builder
public class ChatMessage {
    @Id
    @Column(unique = true, nullable = false, updatable = false)
    private Long chatMessageId;
    @Column(nullable = false, updatable = false)
    private String message;
    @Column(nullable = false, updatable = false)
    private LocalDateTime chatAt;
    @Column(nullable = false, updatable = false)
    private Long userId;
    @Indexed
    @Column(nullable = false, updatable = false)
    private Long chatRoomId;
    @Column(nullable = false, updatable = false)
    private String userDisplayName;
}
