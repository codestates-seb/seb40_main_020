package OneCoin.Server.chat.chatMessage.listener;


import OneCoin.Server.chat.chatMessage.dto.ChatResponseDto;
import OneCoin.Server.exception.BusinessLogicException;
import OneCoin.Server.exception.ExceptionCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisListener {
    private final ObjectMapper objectMapper;
    private final SimpMessagingTemplate messagingTemplate;
    public void sendMessage(String publishedMessage) {
        try {
            //스트링 -> 객체로 매핑
            ChatResponseDto chatResponseDto = objectMapper.readValue(publishedMessage, ChatResponseDto.class);
            log.info("[LISTENER] {}", chatResponseDto.getChatRoomId());
            messagingTemplate.convertAndSend("/topic/rooms/" + chatResponseDto.getChatRoomId(), chatResponseDto);
        } catch (JsonProcessingException e) {
            throw new BusinessLogicException(ExceptionCode.FAIL_TO_SERIALIZE);
        }
    }
}
