package OneCoin.Server.chat.entity;

import lombok.*;

import java.io.Serializable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInChatRoom implements Serializable {
    private String displayName;
    private String email;
}
