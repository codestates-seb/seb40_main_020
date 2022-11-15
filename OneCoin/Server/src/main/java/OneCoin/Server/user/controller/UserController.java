package OneCoin.Server.user.controller;

import OneCoin.Server.dto.SingleResponseDto;
import OneCoin.Server.user.dto.UserDto;
import OneCoin.Server.user.entity.User;
import OneCoin.Server.user.mapper.UserMapper;
import OneCoin.Server.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/members")
@Validated
@Slf4j
public class UserController {
    private UserService userService;
    private UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
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
                new SingleResponseDto<>(userMapper.userToUserResponse(updatedUser)), HttpStatus.CREATED
        );
    }
}
