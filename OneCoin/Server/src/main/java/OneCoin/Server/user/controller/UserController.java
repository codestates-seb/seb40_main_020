package OneCoin.Server.user.controller;

import OneCoin.Server.dto.PageResponseDto;
import OneCoin.Server.dto.SingleResponseDto;
import OneCoin.Server.user.dto.UserDto;
import OneCoin.Server.user.entity.User;
import OneCoin.Server.user.mapper.UserMapper;
import OneCoin.Server.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@Validated
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    // 이메일 인증
    @PostMapping("/authentication-email")
    public ResponseEntity authenticationEmail(@Valid @RequestBody UserDto.Post requestBody) {
        userService.authenticationEmail(userMapper.userPostToUser(requestBody));

        return new ResponseEntity<>(
                new SingleResponseDto<>("Send Email"), HttpStatus.CREATED
        );
    }

    @PostMapping
    public ResponseEntity postUser(@Valid @RequestBody UserDto.Post requestBody) {
        User user = userService.createUser(userMapper.userPostToUser(requestBody));

        return new ResponseEntity<>(
                new SingleResponseDto<>(userMapper.userToUserResponse(user)), HttpStatus.CREATED
        );
    }

    @PatchMapping("/{user-id}")
    public ResponseEntity patchUser(
            @PathVariable("user-id") @Positive long userId,
            @Valid @RequestBody UserDto.Patch requestBody) {
        User user = userMapper.userPatchToUser(requestBody);
        user.setUserId(userId);

        User updatedUser = userService.updateUser(user);

        return new ResponseEntity<>(
                new SingleResponseDto<>(userMapper.userToUserResponse(updatedUser)), HttpStatus.OK
        );
    }

    // 비밀번호 재설정
    @PatchMapping("/reset-passwords")
    public ResponseEntity patchUser(
            @Valid @RequestBody UserDto.Password requestBody,
            @AuthenticationPrincipal Map<String, Object> userInfo) {
        User user = userMapper.userPasswordToUser(requestBody);
        user.setUserId(Long.parseLong(userInfo.get("id").toString()));

        User updatedUser = userService.resetPassword(user);

        return new ResponseEntity<>(
                new SingleResponseDto<>(userMapper.userToUserResponse(updatedUser)), HttpStatus.OK
        );
    }

    // 닉네임 중복 체크
    @GetMapping("/duplicate-display-name")
    public ResponseEntity checkDisplayName(@Valid @RequestParam String displayName) {
        return new ResponseEntity<>(userService.checkDuplicateDisplayName(displayName), HttpStatus.OK);
    }

    // 이메일 중복 체크 error 로 변경
    @GetMapping("/duplicate-email")
    public ResponseEntity checkEmail(@Valid @RequestParam String email) {
        return new ResponseEntity<>(userService.checkDuplicateEmail(email), HttpStatus.OK);
    }

    // 모든 회원 정보
    @GetMapping
    public ResponseEntity getUsers(@Positive @RequestParam int page,
                                   @Positive @RequestParam int size) {
        Page<User> userPage = userService.findUsers(page - 1, size);
        List<User> users = userPage.getContent();
        return new ResponseEntity<>(
                new PageResponseDto<>(userMapper.usersToUserResponses(users),
                        userPage),
                HttpStatus.OK);
    }

    // 단일 회원 정보
    @GetMapping("/{user-id}")
    public ResponseEntity getUser(@PathVariable("user-id") @Positive long userId) {
        User user = userService.findUser(userId);
        return new ResponseEntity<>(new SingleResponseDto<>(userMapper.userToUserResponse(user)), HttpStatus.OK);
    }

    // 이메일 인증 확인
    @GetMapping("/authentication-email/{password}")
    public ResponseEntity authenticationEmail(@PathVariable("password") String password,
                                              @Valid @RequestBody UserDto.Post requestBody) {
        userService.authenticationEmail(userMapper.userPostToUser(requestBody));

        return new ResponseEntity<>(
                new SingleResponseDto<>("Send Email"), HttpStatus.CREATED
        );
    }

}
