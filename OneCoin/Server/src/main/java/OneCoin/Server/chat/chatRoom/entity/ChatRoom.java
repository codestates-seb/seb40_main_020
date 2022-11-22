package OneCoin.Server.chat.chatRoom.entity;

import lombok.*;
import org.hibernate.procedure.spi.ParameterRegistrationImplementor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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
    @OneToMany(mappedBy = "chatRoom", cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE},
    orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<ChatRoomUser> chatRoomUsers = new HashSet<>();
    @Builder
    public ChatRoom(String name, String nation) {
        this.name = name;
        this.nation = nation;
    }
    public void addChatRoomUser(ChatRoomUser chatRoomUser) {
        this.chatRoomUsers.add(chatRoomUser);
    }
}
