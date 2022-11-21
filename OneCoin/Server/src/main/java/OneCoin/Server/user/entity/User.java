package OneCoin.Server.user.entity;

import OneCoin.Server.audit.Auditable;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "users")
public class User extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String displayName;

    @Column(nullable = false, updatable = false, unique = false)
    private String email;

    @Column(nullable = false)
    private String password;

    // 추후 Attribute Converter 사용 고려 -> 사용
    @Column
    @Enumerated(EnumType.STRING)
    private Platform platform;

    @Column(nullable = false)
    private long balance;

    // 추후 Attribute Converter 사용하거나 테이블 분리도 고려 -> 테이블 병합, 컨버터 사용
    @Column
    @Enumerated(EnumType.STRING)
    private Role userRole;
}
