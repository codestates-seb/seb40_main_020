package OneCoin.Server.webSocket.dto;

import OneCoin.Server.webSocket.service.ChatService;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;
@Getter
public class ChatRoom {
    private String roomId;
    private String name;
    //트래픽이 많고, 멀티 쓰레드 환경인경우, 동기화된 컬렉션으로 바꿔야함.
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoom(String roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }

    public void handleActions(WebSocketSession session, ChatMessage chatMessage, ChatService chatService) {
        if(chatMessage.getType().equals(ChatMessage.MessageType.ENTER)) {
            sessions.add(session);
            chatMessage.setMessage(chatMessage.getSender() + "님이 입장했습니다.");
        }
        sendMessage(chatMessage, chatService);
    }

    public <T> void sendMessage(T message, ChatService chatService) {
        sessions.parallelStream().forEach(s -> chatService.sendMessage(s, message));
    }
}
