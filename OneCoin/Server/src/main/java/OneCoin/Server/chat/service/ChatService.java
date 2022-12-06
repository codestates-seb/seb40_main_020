package OneCoin.Server.chat.service;

import OneCoin.Server.chat.entity.ChatMessage;
import OneCoin.Server.chat.entity.ChatRoom;
import OneCoin.Server.chat.repository.ChatMessageRdbRepository;
import OneCoin.Server.chat.repository.ChatMessageRepository;
import OneCoin.Server.chat.constant.MessageType;
import OneCoin.Server.chat.repository.LastSavedRepository;
import OneCoin.Server.chat.repository.LastSentScoreRepository;
import OneCoin.Server.config.auth.utils.UserUtilsForWebSocket;
import OneCoin.Server.exception.BusinessLogicException;
import OneCoin.Server.exception.ExceptionCode;
import OneCoin.Server.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final UserUtilsForWebSocket userInfoUtils;
    private final ChatMessageRdbRepository chatMessageRdbRepository;
    private final LastSavedRepository lastSavedRepository;
    private final ChatRoomService chatRoomService;
    private final LastSentScoreRepository lastSentScoreRepository;
    private final String NOTHING_LEFT_TO_SEND = "NOTHING_LEFT_TO_SEND";
    private final String RDB_PREFIX = "index";
    private final String CHATROOM_PREFIX = "ChatRoom";
    private final String SESSION_PREFIX = "SessionId";

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
        Integer id = (Integer) claims.get("id");
        Long userId = id.longValue();
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

    public List<ChatMessage> getChatMessages(Integer chatRoomId, String sessionId) {
        String scoreAsString = lastSentScoreRepository.get(makeLastSentKey(chatRoomId, sessionId));
        if(isNothingLeftToSend(scoreAsString)) {
            throw new BusinessLogicException(ExceptionCode.NO_CHAT_IN_RDB_EXIST);
        }
        if(isRdbRetrieving(scoreAsString)) {
            Long lastSentIndex = Long.parseLong(scoreAsString.replace(RDB_PREFIX, ""));
            return getMessagesFromRdb(chatRoomId, lastSentIndex - 1L, sessionId);
        }
        double lastSentScore = getNextScoreOfRecentlySent(scoreAsString);
        Double oldestScoreToSend = chatMessageRepository.getScoreOfLastChatWithLimitN(chatRoomId, lastSentScore);
        if (isNoChatLeftInCache(oldestScoreToSend)) {
            return getMessagesFromRdbFirstTime(chatRoomId, lastSentScore, sessionId);
        }
        lastSentScoreRepository.save(makeLastSentKey(chatRoomId, sessionId), oldestScoreToSend.toString());
        return chatMessageRepository.getMessagesFromRoomByScore(chatRoomId, oldestScoreToSend, lastSentScore);
    }
    private boolean isRdbRetrieving(String lastSentScoreAsString) {
        if(lastSentScoreAsString != null && lastSentScoreAsString.startsWith(RDB_PREFIX)) return true;
        return false;
    }
    private boolean isNothingLeftToSend(String lastSentScoreAsString) {
        if(lastSentScoreAsString != null && lastSentScoreAsString.equals(NOTHING_LEFT_TO_SEND)) return true;
        return false;
    }

    private List<ChatMessage> getMessagesFromRdbFirstTime(Integer chatRoomId, Double lastSentScore, String sessionId) {
        List<ChatMessage> lastSentMessage = chatMessageRepository.getWithScore(chatRoomId, lastSentScore + 1L);
        Long lastSentIndex = getSmallestIndex(lastSentMessage);
        return getMessagesFromRdb(chatRoomId, lastSentIndex, sessionId);
    }

    private List<ChatMessage> getMessagesFromRdb(Integer chatRoomId, Long lastSentIndex, String sessionId) {
        List<ChatMessage> messagesToSend = chatMessageRdbRepository.findTop30ByChatRoomIdAndChatMessageIdLessThanEqualOrderByChatMessageIdDesc(
                chatRoomId, lastSentIndex);
        if(messagesToSend.size() == 0) {
            lastSentScoreRepository.save(makeLastSentKey(chatRoomId, sessionId), NOTHING_LEFT_TO_SEND);
            return null;
        }
        Long oldestIndexToSend = messagesToSend.get(messagesToSend.size() - 1).getChatMessageId();
        lastSentScoreRepository.save(makeLastSentKey(chatRoomId, sessionId), RDB_PREFIX + oldestIndexToSend);
        return messagesToSend;
    }

    private double getNextScoreOfRecentlySent(String recentScoreAsString) {
        if (recentScoreAsString == null) {
            return Double.MAX_VALUE;
        }
        return Double.parseDouble(recentScoreAsString) - 1L;
    }

    private Long getSmallestIndex(List<ChatMessage> messages) {
        Long index = Long.MAX_VALUE;
        for(ChatMessage chatMessage : messages) {
            Optional<ChatMessage> foundMessage = chatMessageRdbRepository.findByMessageAndChatAtAndUserId(chatMessage.getMessage(), chatMessage.getChatAt(), chatMessage.getUserId());
            if(foundMessage.isPresent() && foundMessage.get().getChatMessageId() < index) {
                index = foundMessage.get().getChatMessageId();
            }
        }
        if(index != Long.MAX_VALUE && index - 1L != 0L) return index;
        return Long.MAX_VALUE;
    }
    public boolean isNoChatLeftInCache(Double scoreOfLastChat) {
        if (scoreOfLastChat == null) return true;
        return false;
    }
    public void saveInMemoryChatMessagesToRdb() {
        List<ChatRoom> chatRooms = chatRoomService.findAllChatRooms();
        log.debug("chatRooms size : " + chatRooms.size());
        for(ChatRoom chatRoom : chatRooms) {
            Integer chatRoomId = chatRoom.getChatRoomId();
            String recentScore = lastSavedRepository.get(chatRoomId);
            log.debug("lastSavedScore : " + (recentScore == null ? "null" : recentScore));
            List<ChatMessage> messages;
            if(recentScore == null) {
                messages = chatMessageRepository.findAllInAscOrder(chatRoomId);
                log.debug("messages found size : " + messages.size());
                Double latestScore = chatMessageRepository.getScoreOfLatestChat(chatRoomId, Double.MIN_VALUE);
                log.debug("latestScore to save" + (latestScore == null ? "null" : latestScore));
                if(latestScore == null) return;
                lastSavedRepository.save(chatRoomId, latestScore.toString());
            } else {
                double recentScoreAsDouble = Double.parseDouble(recentScore);
                Double latestScore = chatMessageRepository.getScoreOfLatestChat(chatRoomId, recentScoreAsDouble);
                if(latestScore == null) return;
                log.debug("latestScore to save" + (latestScore == null ? "null" : latestScore));
                messages = chatMessageRepository.findByScoreInAcsOrder(chatRoomId, recentScoreAsDouble, latestScore);
                log.debug("messages found size : " + messages.size());
                lastSavedRepository.save(chatRoomId, latestScore.toString());
            }
            if(messages.size() == 0) return;
            chatMessageRdbRepository.saveAll(messages);
        }
    }

    public void deleteInMemory() {
        chatMessageRepository.removeAllInChatRoom(1);
        chatMessageRepository.removeAllInChatRoom(2);
    }

    public void deleteLastSentInfo(String sessionId) {
        lastSentScoreRepository.delete(makeLastSentKey(1, sessionId));
        lastSentScoreRepository.delete(makeLastSentKey(2, sessionId));
    }

    private String makeLastSentKey(Integer chatRoomId, String sessionId) {
        return CHATROOM_PREFIX + chatRoomId + SESSION_PREFIX + sessionId;
    }
}
