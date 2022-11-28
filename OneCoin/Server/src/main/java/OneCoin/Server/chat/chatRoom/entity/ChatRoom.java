package OneCoin.Server.chat.chatRoom.entity;

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
