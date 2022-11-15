package OneCoin.Server.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Post {
        @NotBlank
        private String displayName;

        @NotBlank
        @Email
        private String email;

        // 정규표현식 제약조건 추가 필요
        @NotBlank
        private String password;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Patch {
        @NotBlank
        private String displayName;

        @NotBlank
        @Email
        private String email;

        // 정규표현식 제약조건 추가 필요
        @NotBlank
        private String password;

        private long balance;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private long id;

        private String displayName;

        private String email;
    }

}
