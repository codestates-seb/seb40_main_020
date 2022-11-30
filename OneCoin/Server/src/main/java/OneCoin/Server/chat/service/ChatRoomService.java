package OneCoin.Server.chat.service;


import OneCoin.Server.chat.entity.ChatRoom;
import OneCoin.Server.chat.entity.UserInChatRoom;
import OneCoin.Server.chat.mapper.ChatRoomMapper;
import OneCoin.Server.chat.repository.ChatRoomRepository;
import OneCoin.Server.chat.repository.UserInChatRoomRepository;
import OneCoin.Server.chat.utils.ChatRoomUtils;
import OneCoin.Server.chat.vo.UserInfoInChatRoom;
import OneCoin.Server.exception.BusinessLogicException;
import OneCoin.Server.exception.ExceptionCode;
import OneCoin.Server.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserInChatRoomRepository userInChatRoomRepository;
    private final ChatRoomUtils chatRoomUtils;
    private final ChatRoomMapper mapper;



    public List<ChatRoom> findAllChatRooms() {
        Set<String> chatRoomKeys = chatRoomRepository.findAll();
        return chatRoomKeys.stream()
                .map(chatRoomIdKey -> makeChatRoomByKey(chatRoomIdKey))
                .collect(Collectors.toList());
    }


    private ChatRoom makeChatRoomByKey(String key) {
        Integer chatRoomId = chatRoomUtils.parseChatRoomId((String) key);
        long numberOfSessions = userInChatRoomRepository.getNumberOfUserInChatRoom(chatRoomId);
        return ChatRoom.builder()
                .chatRoomId(chatRoomId)
                .numberOfChatters(numberOfSessions)
                .build();
    }
    public long getNumberOfUserInChatRoom(Integer chatRoomId) {
        findVerifiedChatRoom(chatRoomId);
        return userInChatRoomRepository.getNumberOfUserInChatRoom(chatRoomId);
    }
    public void saveUserToChatRoom(Integer chatRoomId, String sessionId) {
        saveUserToChatRoom(chatRoomId, sessionId, null);
    }

    public void saveUserToChatRoom(Integer chatRoomId, String sessionId, User user) {
        boolean isValid = chatRoomRepository.contains(chatRoomId);
        if (!isValid) {
            makeChatRoom(chatRoomId);
        }
        UserInChatRoom userInChatRoom = null;
        if (user != null) {
            userInChatRoom = mapper.userToUserInChatRoom(user);
        }
        userInChatRoomRepository.addUser(chatRoomId, sessionId, userInChatRoom);
    }

    public void makeChatRoom(Integer chatRoomId) {
        Set<String> chatRooms = chatRoomRepository.findAll();
        int numberOfChatRooms = chatRooms.size();
        if (numberOfChatRooms + 1 != chatRoomId) {
            throw new BusinessLogicException(ExceptionCode.INVALID_CHAT_ROOM_ID);
        }
        chatRoomRepository.create(chatRoomId);
    }

    public UserInfoInChatRoom deleteUserFromChatRoom(String sessionId) {
        Set<String> chatRoomKeys = chatRoomRepository.findAll();
        UserInfoInChatRoom user = new UserInfoInChatRoom();
        chatRoomKeys.stream().forEach(chatRoomKey -> {
            boolean doesContain = userInChatRoomRepository.contain(chatRoomKey, sessionId);
            if(doesContain) {
                user.setChatRoomId(chatRoomUtils.parseChatRoomId(chatRoomKey));
                user.setUser(userInChatRoomRepository.findBySessionId(chatRoomKey, sessionId));
                userInChatRoomRepository.removeUserBySessionId(chatRoomKey, sessionId);
            }
        });
        return user;
    }

    public List<UserInChatRoom> findUsersInChatRoom(Integer chatRoomId) {
        findVerifiedChatRoom(chatRoomId);
        List<UserInChatRoom> users = userInChatRoomRepository.findAllByChatRoomId(chatRoomId);
        List<UserInChatRoom> usersConverted = new ArrayList<>(users);
        usersConverted.removeIf(e -> e == null);
        return usersConverted;
    }

    public void findVerifiedChatRoom(Integer chatRoomId) {
        boolean isValid = chatRoomRepository.contains(chatRoomId);
        if (!isValid) {
            throw new BusinessLogicException(ExceptionCode.INVALID_CHAT_ROOM_ID);
        }
    }
}
