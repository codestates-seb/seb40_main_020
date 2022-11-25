package OneCoin.Server.chat.chatRoom;

import OneCoin.Server.chat.chatRoom.entity.ChatRoomInMemory;
import OneCoin.Server.chat.chatRoom.service.ChatRoomInMemoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NumberOfChattersScheduler {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRoomInMemoryService chatRoomInMemoryService;

    @Scheduled(fixedDelay = 1000)
    public void sendNumberOfChatters() {
        List<ChatRoomInMemory> rooms = chatRoomInMemoryService.getChatRooms();
        messagingTemplate.convertAndSend("/topic/rooms-info", rooms);
    }
}
