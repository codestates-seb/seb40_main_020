package OneCoin.Server.chat.chatMessage.controller;

import OneCoin.Server.chat.chatMessage.dto.ChatRequestDto;
import OneCoin.Server.chat.chatMessage.dto.ChatResponseDto;
import OneCoin.Server.chat.chatMessage.entity.ChatMessage;
import OneCoin.Server.chat.chatMessage.mapper.ChatMapper;
import OneCoin.Server.chat.chatMessage.service.ChatService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
@Slf4j
public class ChatController {
    private final ChatService chatService;
    private final ChatMapper chatMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/rooms")//여기에는 @Payload가 생략되어 있음. body를 객체로 mapping시켜줌
    public void message(ChatRequestDto requestMessage) {
        log.info("message received: {}", requestMessage);
        ChatMessage convertedChatMessage = chatMapper.requestDtoToChatMessage(requestMessage);
        //TODO : delegate를 저장으로 바꿔도 될 것 같은데
        ChatMessage chatMessage = chatService.saveMessage(convertedChatMessage);
        ChatResponseDto chatResponseDto = chatMapper.chatMessageToResponseDto(chatMessage);
        log.info("[CONTROLLER] message is ready for sending");
        log.info("[MESSAGE] {}", chatResponseDto);
        messagingTemplate.convertAndSend("/topic/rooms/" + chatResponseDto.getChatRoomId(), chatResponseDto);
    }
}

