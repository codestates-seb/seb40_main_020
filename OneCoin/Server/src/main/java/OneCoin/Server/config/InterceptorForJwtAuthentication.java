package OneCoin.Server.config;

import OneCoin.Server.chat.chatRoom.entity.ChatRoom;
import OneCoin.Server.chat.chatRoom.entity.ChatRoomUser;
import OneCoin.Server.chat.chatRoom.service.ChatRoomService;
import OneCoin.Server.config.auth.jwt.JwtTokenizer;
import OneCoin.Server.config.auth.utils.CustomAuthorityUtils;
import OneCoin.Server.config.auth.utils.LoggedInUserInfoUtilsForWebSocket;
import OneCoin.Server.exception.BusinessLogicException;
import OneCoin.Server.exception.ExceptionCode;
import OneCoin.Server.user.entity.Role;
import OneCoin.Server.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
@Slf4j
@RequiredArgsConstructor
public class InterceptorForJwtAuthentication implements ChannelInterceptor {
    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils customAuthorityUtils;
    private final LoggedInUserInfoUtilsForWebSocket loggedInUserInfoUtilsForWebSocket;
    private final ChatRoomService chatRoomService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            verifyAuthorization(accessor);
        } else if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            /** 이 부분을 redis에 저장할지 고민입니다.*/
            ChatRoom chatRoom = registerUserToChatRoom(accessor);
            System.out.println(chatRoom);
//            sendEnterMessage();
        }
        return message;
    }
    @Transactional
    private ChatRoom registerUserToChatRoom(StompHeaderAccessor accessor) {
        long roomId = parseRoomIdFromHeader(accessor);
        User user = loggedInUserInfoUtilsForWebSocket.extractUser(accessor.getUser());
        ChatRoom chatRoom = chatRoomService.findChatRoom(roomId);
        ChatRoomUser chatRoomUser = ChatRoomUser.builder()
                .chatRoom(chatRoom)
                .user(user)
                .build();
        chatRoom.addChatRoomUser(chatRoomUser);
        return chatRoomService.updateChatRoom(chatRoom);
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

    private void verifyAuthorization(StompHeaderAccessor accessor) {
        String authorizationValue = accessor.getFirstNativeHeader("Authorization");
        if (authorizationValue == null || authorizationValue.equals("null")) return;
        String jws = authorizationValue.replace("Bearer ", "");
        Map<String, Object> claims = verifyJws(jws);
        Authentication user = setAndGetAuthentication(claims);
        accessor.setUser(user);
        accessor.setLeaveMutable(true);
    }
    private Map<String, Object> verifyJws(String jws) {
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey()); // SecretKey 파싱
        Map<String, Object> claims = jwtTokenizer.getClaims(jws, base64EncodedSecretKey).getBody();   // Claims 파싱
        return claims;
    }

    private Authentication setAndGetAuthentication(Map<String, Object> claims) {
        List<GrantedAuthority> authorities = customAuthorityUtils.createAuthorities(Role.valueOf((String) claims.get("roles")));
        Authentication authentication = new UsernamePasswordAuthenticationToken(claims, null, authorities);
//        SecurityContextHolder.getContext().setAuthentication(authentication); // SecurityContext 에 Authentication 저장 이게 웹소켓에서는 안먹힘
        return authentication;
    }
}
