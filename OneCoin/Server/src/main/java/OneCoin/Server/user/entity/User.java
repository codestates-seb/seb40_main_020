package OneCoin.Server.user.entity;

import OneCoin.Server.audit.Auditable;
import lombok.*;
import OneCoin.Server.balance.entity.Balance;
import OneCoin.Server.chat.chatRoom.entity.ChatRoomUser;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "users")
@Builder
public class User extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String displayName;

    @Column(nullable = false, updatable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String imagePath;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Balance balance;

    // 추후 Attribute Converter 사용 고려 -> 사용
    @Column
    @Enumerated(EnumType.STRING)
    private Platform platform;

    // 추후 Attribute Converter 사용하거나 테이블 분리도 고려 -> 테이블 병합, 컨버터 사용
    @Column
    @Enumerated(EnumType.STRING)
    private Role userRole;
    @OneToMany(mappedBy = "chatRoom", cascade = {CascadeType.REMOVE, CascadeType.MERGE, CascadeType.DETACH},
            orphanRemoval = true)
    private Set<ChatRoomUser> chatRoomUsers;

    @Builder
    public User(String displayName, String email, String password, Platform platform, Role userRole) {
        this.displayName = displayName;
        this.email = email;
        this.password = password;
        this.platform = platform;
        this.userRole = userRole;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.email);
    }
}
