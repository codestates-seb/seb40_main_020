package OneCoin.Server.chat.controller;

import OneCoin.Server.config.auth.utils.UserUtilsForWebSocket;
import OneCoin.Server.exception.BusinessLogicException;
import OneCoin.Server.exception.ExceptionCode;
import OneCoin.Server.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

@Slf4j
@RequiredArgsConstructor
public class MessageInterceptor implements ChannelInterceptor {
    private final UserUtilsForWebSocket userUtilsForWebSocket;
    private final RegisterController registerController;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        StompCommand command = accessor.getCommand();
        String sessionId = accessor.getSessionId();
        if (StompCommand.CONNECT.equals(command)) {
            log.info("[CONNECT] start {}", sessionId);
            registerController.authenticate(accessor);
            log.info("[CONNECT] complete {}", sessionId);
        } else if (StompCommand.SUBSCRIBE.equals(command)) {
            log.info("[SUBSCRIBE] start {}", sessionId);
            Integer chatRoomId = parseRoomIdFromHeader(accessor); //채팅 구독이 아니면 null반환
            User user = userUtilsForWebSocket.extractUser(accessor.getUser()); //비회원은 null을 반환
            registerController.registerUserAndSendEnterMessage(sessionId, chatRoomId, user);
            log.info("[SUBSCRIBE] complete {}", sessionId);
        } else if (StompCommand.UNSUBSCRIBE.equals(command)) {
            log.info("[UNSUBSCRIBE] start {}", sessionId);
            registerController.unregisterUserAndSendLeaveMessage(sessionId);
            log.info("[UNSUBSCRIBE] complete {}", sessionId);
        } else if (StompCommand.DISCONNECT.equals(command)) {
            log.info("[DISCONNECT] start {}", sessionId);
            registerController.unregisterUserAndSendLeaveMessage(sessionId);
            log.info("[DISCONNECT] complete {}", sessionId);
        }
        return message;
    }


    private Integer parseRoomIdFromHeader(StompHeaderAccessor accessor) {
        try {
            String destination = accessor.getDestination();
            String[] temp = destination.split("/");
            if (!temp[temp.length - 2].equals("rooms")) {
                return null;
            }
            return Integer.parseInt(temp[temp.length - 1]);
        } catch (Exception e) {
            throw new BusinessLogicException(ExceptionCode.INVALID_DESTINATION);
        }
    }


}
