package OneCoin.Server.chat.chatMessage.service;

import OneCoin.Server.chat.chatMessage.entity.ChatMessage;
import OneCoin.Server.chat.chatMessage.repository.ChatMessageRepository;
import OneCoin.Server.chat.chatRoom.service.ChatRoomService;
import OneCoin.Server.chat.constant.MessageType;
import OneCoin.Server.exception.BusinessLogicException;
import OneCoin.Server.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatService {
    //타입으로 분기하기
    //시간추가하기
    //데이터베이스에저장하기
    //유효성 검사하기
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;
    private Map<Long, ChannelTopic> topics = new HashMap<>();
    public ChatMessage delegate(MessageType messageType, ChatMessage chatMessage) {
        switch (messageType) {
            case ENTER:
                chatMessage = enterRoom(chatMessage);
                break;
            case LEAVE:
                chatMessage = leaveRoom(chatMessage);
                break;
        }
        chatMessage.setChatAt(LocalDateTime.now());
        return chatMessageRepository.save(chatMessage);
    }
    private ChatMessage enterRoom(ChatMessage chatMessage) {
        chatMessage.setMessage("[알림] " + chatMessage.getUserDisplayName() + "이 입장하셨습니다.");
        return chatMessage;
    }
    private ChatMessage leaveRoom(ChatMessage chatMessage) {
        chatMessage.setMessage("[알림] " + chatMessage.getUserDisplayName() + "이 퇴장하셨습니다.");
        return chatMessage;
    }
}
