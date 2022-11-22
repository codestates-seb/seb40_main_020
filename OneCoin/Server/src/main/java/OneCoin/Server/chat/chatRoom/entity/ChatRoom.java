package OneCoin.Server.chat.chatRoom.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ChatRoom {
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;
    private String name;
    private String nation;

    @Builder
    public ChatRoom(String name, String nation) {
        this.name = name;
        this.nation = nation;
    }
}
