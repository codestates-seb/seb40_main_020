package OneCoin.Server.chat.chatRoom.service;

import OneCoin.Server.chat.chatRoom.dto.ChatRoomDto;
import OneCoin.Server.chat.chatRoom.entity.ChatRoom;
import OneCoin.Server.chat.chatRoom.entity.ChatRoomUser;
import OneCoin.Server.chat.chatRoom.repository.ChatRoomRepository;
import OneCoin.Server.chat.chatRoom.repository.ChatRoomUserRepository;
import OneCoin.Server.chat.constant.Nation;
import OneCoin.Server.exception.BusinessLogicException;
import OneCoin.Server.exception.ExceptionCode;
import OneCoin.Server.user.entity.User;
import OneCoin.Server.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@AllArgsConstructor
public class ChatRoomService {
    private ChatRoomRepository chatRoomRepository;
    private UserService userService;
    private ChatRoomUserRepository chatRoomUserRepository;

    //채팅방번호로 조회
    public ChatRoom findChatRoom(Long roomId) {
        ChatRoom verifiedRoom = findVerifiedRoom(roomId);
        return verifiedRoom;
    }

    //채팅 국가로 조회
    public List<ChatRoom> findRoomByNation(String nation) {
        Nation.verifyNation(nation);
        List<ChatRoom> rooms = chatRoomRepository
                .findByNationOrderByChatRoomIdDesc(nation);
        return rooms;
    }

    //채팅방 생성
    public ChatRoom createRoom(String name, String nation) {
        Nation.verifyNation(nation);
        checkDuplicatedName(name);
        ChatRoom chatRoom = ChatRoom.builder().name(name).nation(nation).build();
        return chatRoomRepository.save(chatRoom);
    }

    public List<ChatRoomDto> findAll() {
        return chatRoomRepository.findAllRegisteredUserNumberGroupByChatRoom();
    }

    //존재하는지 확인
    private ChatRoom findVerifiedRoom(Long roomId) {
        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findById(roomId);
        return optionalChatRoom
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.NO_SUCH_CHAT_ROOM));
    }

    //채팅방이름 중복 확인
    private void checkDuplicatedName(String name) {
        Optional<ChatRoom> chatRoom = chatRoomRepository.findByName(name);
        if (chatRoom.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.CHAT_ROOM_NAME_EXISTS);
        }
    }

    //updateChatRoom
    @Transactional
    public ChatRoom updateChatRoom(ChatRoom chatRoom) {
        return chatRoomRepository.save(chatRoom);
    }

    @Transactional
    public ChatRoom registerUserToChatRoom(long chatRoomId, User user) {
        ChatRoom chatRoom = findChatRoom(chatRoomId);
        ChatRoomUser chatRoomUser = ChatRoomUser.builder()
                .chatRoom(chatRoom)
                .user(user)
                .build();
        chatRoom.addChatRoomUser(chatRoomUser);
        return updateChatRoom(chatRoom);
    }

    @Transactional
    public List<Long> unregisterUserFromChatRoom(long userId) {
        User user = userService.findUser(userId);
        Set<ChatRoomUser> chatRoomUsers = user.getChatRoomUsers();
        List<Long> chatRoomIds = new ArrayList<>();
        for (ChatRoomUser chatRoomUser : chatRoomUsers) {
            chatRoomIds.add(chatRoomUser.getChatRoom().getChatRoomId());
            chatRoomUserRepository.delete(chatRoomUser);
        }
        return chatRoomIds;
    }
}
