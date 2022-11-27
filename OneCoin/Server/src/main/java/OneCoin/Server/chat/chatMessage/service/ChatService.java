package OneCoin.Server.chat.chatMessage.service;

import OneCoin.Server.chat.chatMessage.entity.ChatMessage;
import OneCoin.Server.chat.chatMessage.repository.ChatMessageRepository;
import OneCoin.Server.chat.constant.MessageType;
import OneCoin.Server.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    //타입으로 분기하기
    //시간추가하기
    //데이터베이스에저장하기
    //유효성 검사하기
    private final ChatMessageRepository chatMessageRepository;
    private final Long NUMBER_OF_CHATS_TO_SEND = 30L;

    public ChatMessage makeEnterOrLeaveChatMessage(MessageType messageType, Integer chatRoomId, User user) {
        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoomId(chatRoomId)
                .userId(user.getUserId())
                .userDisplayName(user.getDisplayName())
                .build();
        if(messageType.equals(MessageType.ENTER)) {
            setEnterMessage(chatMessage);
        } else if(messageType.equals(MessageType.LEAVE)) {
            setLeaveMessage(chatMessage);
        }
        setCurrentTime(chatMessage);
        return chatMessage;
    }

    public ChatMessage saveMessage(ChatMessage chatMessage) {
        setCurrentTime(chatMessage);
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    private ChatMessage setCurrentTime(ChatMessage chatMessage) {
        chatMessage.setChatAt(LocalDateTime.now().toString());
        return chatMessage;
    }

    private ChatMessage setEnterMessage(ChatMessage chatMessage) {
        chatMessage.setMessage("[알림] " + chatMessage.getUserDisplayName() + "이 입장하셨습니다.");
        return chatMessage;
    }

    private ChatMessage setLeaveMessage(ChatMessage chatMessage) {
        chatMessage.setMessage("[알림] " + chatMessage.getUserDisplayName() + "이 퇴장하셨습니다.");
        return chatMessage;
    }

    public List<ChatMessage> getChatMessages(Integer chatRoomId) {
        return chatMessageRepository.getMessageFromRoomLimitN(chatRoomId, NUMBER_OF_CHATS_TO_SEND);
    }
}
