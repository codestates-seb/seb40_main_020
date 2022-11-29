package OneCoin.Server.chat.chatRoom.service;

import OneCoin.Server.chat.chatRoom.entity.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NumberOfChattersScheduler {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRoomService chatRoomService;

    @Scheduled(fixedDelay = 1000)
    public void sendNumberOfChatters() {
        List<ChatRoom> rooms = chatRoomService.getChatRooms();
        messagingTemplate.convertAndSend("/topic/rooms-info", rooms);
    }
}
