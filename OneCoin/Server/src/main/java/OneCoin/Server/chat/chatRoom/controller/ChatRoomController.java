package OneCoin.Server.chat.chatRoom.controller;

import OneCoin.Server.chat.chatMessage.dto.ChatResponseDto;
import OneCoin.Server.chat.chatMessage.entity.ChatMessage;
import OneCoin.Server.chat.chatMessage.mapper.ChatMapper;
import OneCoin.Server.chat.chatMessage.service.ChatService;
import OneCoin.Server.chat.chatRoom.dto.UsersInRoomResponseDto;
import OneCoin.Server.chat.chatRoom.entity.UserInChatRoom;
import OneCoin.Server.chat.chatRoom.service.ChatRoomService;
import OneCoin.Server.dto.MultiResponseDto;
import OneCoin.Server.dto.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity getMessagesInRoom(@PathVariable("room-id") Integer chatRoomId) {
        List<ChatMessage> messages = chatService.getChatMessages(chatRoomId);
        List<ChatResponseDto> responses = chatMapper.chatMessagesToResponseDtos(messages);
        return new ResponseEntity<>(new MultiResponseDto<>(responses), HttpStatus.CREATED);
    }
}
