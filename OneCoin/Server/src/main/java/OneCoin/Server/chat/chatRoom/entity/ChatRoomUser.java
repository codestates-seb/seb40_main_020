package OneCoin.Server.chat.chatRoom.entity;

import OneCoin.Server.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChatRoomUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomUserId;
    @ManyToOne
    @JoinColumn(name = "chat_room_user_chat_room_id")
    private ChatRoom chatRoom;
    @ManyToOne
    @JoinColumn(name = "chat_room_user_user_id")
    private User user;

    @Override
    public int hashCode() {
        return this.chatRoom.hashCode() * this.user.hashCode() * 31;
    }

    @Override
    public boolean equals(Object obj) {
        ChatRoomUser chatRoomUser = (ChatRoomUser) obj;
        if (this.user.getEmail().equals(chatRoomUser.getUser().getEmail())
                && this.chatRoom.getName().equals(chatRoomUser.getChatRoom().getName())) {
            return true;
        }
        return false;
    }
}
