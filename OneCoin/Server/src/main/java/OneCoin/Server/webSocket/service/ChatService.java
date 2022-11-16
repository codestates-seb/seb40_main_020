package OneCoin.Server.webSocket.service;

import OneCoin.Server.webSocket.dto.ChatRoom;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {
    private final ObjectMapper objectMapper;
    private Map<String, ChatRoom> chatRooms = new ConcurrentHashMap<>();
    //채팅방 조회
    public List<ChatRoom> findAllRoom() {
        return new ArrayList<>(chatRooms.values());
    }
    //채팅방번호로 조회
    public ChatRoom findRoomById(String roomId) {
        return chatRooms.get(roomId);
    }
    //채팅방 생성
    public ChatRoom createRoom(String name) {
        String randomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(randomId)
                .name(name)
                .build();
        chatRooms.put(randomId, chatRoom);
        return chatRoom;
    }
    //메시지 전송
    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }
    }
}
