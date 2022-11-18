package OneCoin.Server.chat.chatMessage.controller;

import OneCoin.Server.chat.chatMessage.dto.ChatMessageDto;
import OneCoin.Server.chat.chatRoom.service.ChatRoomService;
import OneCoin.Server.chat.constant.MessageType;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.validation.Valid;

@Controller
@AllArgsConstructor
public class ChatController {
    private final SimpMessagingTemplate template;
    private final ChatRoomService chatRoomService;

    @MessageMapping("/rooms")//여기에는 @Payload가 생략되어 있음. body를 객체로 mapping시켜줌
    public void message(@Valid ChatMessageDto message) {
        verifyRoom(message.getRoomId());
        if(message.getType().equals(MessageType.ENTER)) {
            message.setMessage(message.getSender()+"님이 입장하였습니다");
        }
        template.convertAndSend("/topic/room/" + message.getRoomId(),message);
    }

    private void verifyRoom(long roomId) {
        chatRoomService.findChatRoom(roomId);
    }
}

