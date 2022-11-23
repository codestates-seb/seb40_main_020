package OneCoin.Server.config;

import OneCoin.Server.chat.chatMessage.controller.ChatController;
import OneCoin.Server.chat.chatMessage.dto.ChatRequestDto;
import OneCoin.Server.chat.chatRoom.service.ChatRoomService;
import OneCoin.Server.chat.constant.MessageType;
import OneCoin.Server.config.auth.jwt.JwtTokenizer;
import OneCoin.Server.config.auth.utils.AuthorizationUtilsForWebSocket;
import OneCoin.Server.config.auth.utils.CustomAuthorityUtils;
import OneCoin.Server.config.auth.utils.LoggedInUserInfoUtilsForWebSocket;
import OneCoin.Server.exception.BusinessLogicException;
import OneCoin.Server.exception.ExceptionCode;
import OneCoin.Server.user.entity.User;
import OneCoin.Server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MessageInterceptor implements ChannelInterceptor {
    private final LoggedInUserInfoUtilsForWebSocket loggedInUserInfoUtilsForWebSocket;
    private final ChatRoomService chatRoomService;
    private final ChatController chatController;
    private final UserService userService;
    private final AuthorizationUtilsForWebSocket authorizationUtils;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            log.info("[CONNECT] start", accessor.getDestination());
            authorizationUtils.verifyAuthorization(accessor);
            log.info("verification complete");
        } else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            log.info("[SUBSCRIBE] start {}", accessor.getDestination());
            registerUserAndSendEnterMessage(accessor);
        } else if (StompCommand.SEND.equals(accessor.getCommand())) {
            log.info("[SEND] start");
        } else if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            log.info("[DISCONNECT] start");
            unregisterUserAndSendLeaveMessage(accessor);
        }
        return message;
    }

    private void registerUserAndSendEnterMessage(StompHeaderAccessor accessor) {
        User user = loggedInUserInfoUtilsForWebSocket.extractUser(accessor.getUser());
        long chatRoomId = parseRoomIdFromHeader(accessor);
        chatRoomService.registerUserToChatRoom(chatRoomId, user);
        log.info("register user complete : user {} -> room {}", user.getUserId(), chatRoomId);
        sendEnterMessage(chatRoomId, user);
    }

    private void unregisterUserAndSendLeaveMessage(StompHeaderAccessor accessor) {
        User user = loggedInUserInfoUtilsForWebSocket.extractUser(accessor.getUser());
        List<Long> chatRoomIds = chatRoomService.unregisterUserFromChatRoom(user.getUserId());
        sendLeaveMessage(chatRoomIds, user);
        log.info("unregister user complete : user{} <- room {}", user.getUserId(), chatRoomIds);
    }

    private void sendLeaveMessage(List<Long> chatRoomIds, User user) {
        for (Long chatRoomId : chatRoomIds) {
            ChatRequestDto chatRequestDto = ChatRequestDto.builder()
                    .type(MessageType.LEAVE)
                    .userDisplayName(user.getDisplayName())
                    .userId(user.getUserId())
                    .chatRoomId(chatRoomId)
                    .build();
            chatController.message(chatRequestDto);
        }
    }

    private void sendEnterMessage(long chatRoomId, User user) {
        ChatRequestDto chatRequestDto = ChatRequestDto.builder()
                .type(MessageType.ENTER)
                .userDisplayName(user.getDisplayName())
                .userId(user.getUserId())
                .chatRoomId(chatRoomId)
                .build();
        chatController.message(chatRequestDto);
        log.info("message sent : {}", chatRequestDto);
    }

    private Long parseRoomIdFromHeader(StompHeaderAccessor accessor) {
        try {
            String destination = accessor.getDestination();
            String[] temp = destination.split("/");
            return Long.parseLong(temp[temp.length - 1]);
        } catch (Exception e) {
            throw new BusinessLogicException(ExceptionCode.INVALID_DESTINATION);
        }
    }
}
