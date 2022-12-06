package OneCoin.Server.chat.controller;

import OneCoin.Server.chat.dto.ChatResponseDto;
import OneCoin.Server.chat.entity.ChatMessage;
import OneCoin.Server.chat.mapper.ChatMapper;
import OneCoin.Server.chat.service.ChatService;
import OneCoin.Server.chat.dto.UsersInRoomResponseDto;
import OneCoin.Server.chat.entity.UserInChatRoom;
import OneCoin.Server.chat.service.ChatRoomService;
import OneCoin.Server.dto.MultiResponseDto;
import OneCoin.Server.dto.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ws/chat/rooms")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final ChatService chatService;
    private final ChatMapper chatMapper;

    @GetMapping("/{room-id}/users")
    public ResponseEntity getUsersInRoom(@PathVariable("room-id") Integer chatRoomId) {
        List<UserInChatRoom> users = chatRoomService.findUsersInChatRoom(chatRoomId);
        UsersInRoomResponseDto response = new UsersInRoomResponseDto(chatRoomId, users);
        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.CREATED);
    }

    @GetMapping("/{room-id}/messages")
    public ResponseEntity getMessagesInRoom(@PathVariable("room-id") Integer chatRoomId, @RequestParam String sessionId) {
        List<ChatMessage> messages = chatService.getChatMessages(chatRoomId, sessionId);
        List<ChatResponseDto> responses = chatMapper.chatMessagesToResponseDtos(messages);
        return new ResponseEntity<>(new MultiResponseDto<>(responses), HttpStatus.CREATED);
    }
}
