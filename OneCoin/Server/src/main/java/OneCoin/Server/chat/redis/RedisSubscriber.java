package OneCoin.Server.chat.redis;


import OneCoin.Server.chat.chatMessage.dto.ChatResponseDto;
import OneCoin.Server.exception.BusinessLogicException;
import OneCoin.Server.exception.ExceptionCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber{
    private final ObjectMapper objectMapper;
    private final RedisTemplate redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    //리스너에게 수신된 메시지를 각 비즈니스 로직을 거쳐 WebSocket구독자들에게 전달한다.
    public void sendMessage(String publishedMessage) {
        try {
            //스트링 -> 객체로 매핑
            ChatResponseDto chatResponseDto = objectMapper.readValue(publishedMessage, ChatResponseDto.class);
            messagingTemplate.convertAndSend("/topic/rooms/" + chatResponseDto.getChatRoomId(), chatResponseDto);
        } catch (JsonProcessingException e) {
            throw new BusinessLogicException(ExceptionCode.FAIL_TO_SERIALIZE);
        }
    }
}
