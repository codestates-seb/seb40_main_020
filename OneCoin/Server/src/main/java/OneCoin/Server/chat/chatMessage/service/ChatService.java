package OneCoin.Server.chat.chatMessage.service;

import OneCoin.Server.chat.chatMessage.entity.ChatMessage;
import OneCoin.Server.chat.chatMessage.repository.ChatMessageRepository;
import OneCoin.Server.chat.constant.MessageType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    //타입으로 분기하기
    //시간추가하기
    //데이터베이스에저장하기
    //유효성 검사하기
    private final ChatMessageRepository chatMessageRepository;

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

    public List<ChatMessage> getChatMessages(Long chatRoomId) {
        return chatMessageRepository.findAllByChatRoomId(chatRoomId).stream().limit(30).collect(Collectors.toList());
    }
}
