package OneCoin.Server.chat.chatRoom.service;


import OneCoin.Server.chat.chatRoom.entity.ChatRoomInMemory;
import OneCoin.Server.chat.chatRoom.entity.UserInChatRoomInMemory;
import OneCoin.Server.chat.chatRoom.repository.ChatRoomInMemoryRepository;
import OneCoin.Server.chat.chatRoom.repository.UserInChatRoomRepository;
import OneCoin.Server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomInMemoryService {
    private final ChatRoomInMemoryRepository chatRoomInMemoryRepository;
    private final UserInChatRoomRepository userInChatRoomRepository;
    private final UserService userService;

    public List<ChatRoomInMemory> getChatRooms() {
      return chatRoomInMemoryRepository.findAll();
    }

    public void increaseNumberOfChatters(Long chatRoomId) {
        ChatRoomInMemory chatRoom = findVerifiedChatRoom(chatRoomId);
        chatRoom.increaseNumberOfChatters();
        chatRoomInMemoryRepository.save(chatRoom);
    }

    public void decreaseNumberOfChatters(Long chatRoomId) {
        ChatRoomInMemory chatRoom = findVerifiedChatRoom(chatRoomId);
        chatRoom.decreaseNumberOfChatters();
        chatRoomInMemoryRepository.save(chatRoom);
    }

    public void saveUserInChatRoom(Long chatRoomId, Long userId, String userDisplayName, String email) {
        userService.findVerifiedUser(userId);
        UserInChatRoomInMemory user = UserInChatRoomInMemory.builder()
                .userId(userId)
                .userDisplayName(userDisplayName)
                .chatRoomId(chatRoomId)
                .userEmail(email)
                .build();
        userInChatRoomRepository.save(user);
    }

    public void deleteUserInChatRoom(Long userId, Long chatRoomId) {
        userService.findVerifiedUser(userId);
        List<UserInChatRoomInMemory> users = userInChatRoomRepository.findAllByChatRoomIdAndUserId(chatRoomId, userId);
        userInChatRoomRepository.deleteAll(users);
    }

    public void deleteUserInChatRoom(List<UserInChatRoomInMemory> user) {
        userInChatRoomRepository.deleteAll(user);
    }

    public List<UserInChatRoomInMemory> findUsersInChatRoom(Long chatRoomId) {
        findVerifiedChatRoom(chatRoomId);
        List<UserInChatRoomInMemory> users = userInChatRoomRepository.findAllByChatRoomId(chatRoomId);
        return users;
    }

    public List<UserInChatRoomInMemory> findByUserId(Long userId) {
        userService.findVerifiedUser(userId);
        return userInChatRoomRepository.findAllByUserId(userId);
    }


    private ChatRoomInMemory findVerifiedChatRoom(Long chatRoomId) {
        Optional<ChatRoomInMemory> optionalChatRoom = chatRoomInMemoryRepository.findById(chatRoomId);
        if(optionalChatRoom.isEmpty()) {
            return createChatRoom(chatRoomId);
        }
        return optionalChatRoom.get();
    }

    public ChatRoomInMemory createChatRoom(Long chatRoomId) {
        ChatRoomInMemory chatRoomInMemory = ChatRoomInMemory.builder()
                .chatRoomId(chatRoomId)
                .build();
        return chatRoomInMemoryRepository.save(chatRoomInMemory);
    }
}
