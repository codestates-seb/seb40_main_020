package OneCoin.Server.chat.service;

import OneCoin.Server.chat.entity.ChatRoom;
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
        List<ChatRoom> rooms = chatRoomService.findAllChatRooms();
        messagingTemplate.convertAndSend("/topic/rooms-info", rooms);
    }
}
