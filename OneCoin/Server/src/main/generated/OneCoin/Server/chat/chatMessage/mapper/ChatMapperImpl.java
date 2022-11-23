package OneCoin.Server.chat.chatMessage.mapper;

import OneCoin.Server.chat.chatMessage.dto.ChatRequestDto;
import OneCoin.Server.chat.chatMessage.dto.ChatResponseDto;
import OneCoin.Server.chat.chatMessage.entity.ChatMessage;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-11-22T17:32:03+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 11.0.16 (Azul Systems, Inc.)"
)
@Component
public class ChatMapperImpl implements ChatMapper {

    @Override
    public ChatMessage requestDtoToChatMessage(ChatRequestDto chatRequestDto) {
        if ( chatRequestDto == null ) {
            return null;
        }

        ChatMessage chatMessage = new ChatMessage();

        chatMessage.setMessage( chatRequestDto.getMessage() );
        chatMessage.setUserId( chatRequestDto.getUserId() );
        chatMessage.setChatRoomId( chatRequestDto.getChatRoomId() );
        chatMessage.setUserDisplayName( chatRequestDto.getUserDisplayName() );

        return chatMessage;
    }

    @Override
    public ChatResponseDto chatMessageToResponseDto(ChatMessage chatMessage) {
        if ( chatMessage == null ) {
            return null;
        }

        long chatRoomId = 0L;
        String userDisplayName = null;
        String message = null;
        LocalDateTime chatAt = null;

        if ( chatMessage.getChatRoomId() != null ) {
            chatRoomId = chatMessage.getChatRoomId();
        }
        userDisplayName = chatMessage.getUserDisplayName();
        message = chatMessage.getMessage();
        chatAt = chatMessage.getChatAt();

        ChatResponseDto chatResponseDto = new ChatResponseDto( chatRoomId, userDisplayName, message, chatAt );

        return chatResponseDto;
    }
}
