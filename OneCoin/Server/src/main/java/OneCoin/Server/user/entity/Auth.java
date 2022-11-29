package OneCoin.Server.user.entity;

import OneCoin.Server.audit.CreatedOnlyAuditable;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Auth extends CreatedOnlyAuditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authId;

    @Column(nullable = false)
    private String authPassword;

    @ManyToOne
    @JoinColumn(name = "auth_user_id")
    private User user;
}
