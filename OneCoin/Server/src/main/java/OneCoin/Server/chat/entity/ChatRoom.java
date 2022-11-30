package OneCoin.Server.chat.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatRoom {
    private Integer chatRoomId;
    private Long numberOfChatters;
}
