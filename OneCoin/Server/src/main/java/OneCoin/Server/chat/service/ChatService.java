package OneCoin.Server.chat.service;

import OneCoin.Server.chat.entity.ChatMessage;
import OneCoin.Server.chat.repository.ChatMessageRepository;
import OneCoin.Server.chat.constant.MessageType;
import OneCoin.Server.config.auth.utils.UserUtilsForWebSocket;
import OneCoin.Server.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final UserUtilsForWebSocket userInfoUtils;
    private final Long NUMBER_OF_CHATS_TO_SEND_WHEN_ENTER = 30L;

    public ChatMessage makeEnterOrLeaveChatMessage(MessageType messageType, Integer chatRoomId, User user) {
        ChatMessage chatMessage = ChatMessage.builder()
                .type(messageType)
                .chatRoomId(chatRoomId)
                .userId(user.getUserId())
                .userDisplayName(user.getDisplayName())
                .build();
        if (messageType.equals(MessageType.ENTER)) {
            setEnterMessage(chatMessage);
        } else if (messageType.equals(MessageType.LEAVE)) {
            setLeaveMessage(chatMessage);
        }
        setCurrentTime(chatMessage);
        return chatMessage;
    }

    public ChatMessage setInfoAndSaveMessage(ChatMessage chatMessage, Principal user) {
        chatMessage.setType(MessageType.TALK);
        setUserInfo(chatMessage, user);
        setCurrentTime(chatMessage);
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    private void setUserInfo(ChatMessage chatMessage, Principal user) {
        Map<String, Object> claims = userInfoUtils.extractClaims(user);
        Long userId = ((Integer) claims.get("id")).longValue();
        String displayName = (String) claims.get("displayName");
        chatMessage.setUserId(userId);
        chatMessage.setUserDisplayName(displayName);
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
        return chatMessageRepository.getMessageFromRoomLimitN(chatRoomId, NUMBER_OF_CHATS_TO_SEND_WHEN_ENTER);
    }
}