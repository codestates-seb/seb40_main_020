package OneCoin.Server.chat.chatMessage.entity;


import javax.persistence.Column;
import javax.persistence.Id;
import java.time.LocalDateTime;

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
    @Column(nullable = false, updatable = false)
    private Long chatRoomId;
}
