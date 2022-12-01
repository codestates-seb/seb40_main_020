package OneCoin.Server.chat.entity;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {
    private Integer chatRoomId;
    private Long numberOfChatters;
}
