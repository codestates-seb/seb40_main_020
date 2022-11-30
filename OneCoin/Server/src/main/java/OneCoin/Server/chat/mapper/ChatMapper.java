package OneCoin.Server.chat.mapper;

import OneCoin.Server.chat.dto.ChatRequestDto;
import OneCoin.Server.chat.dto.ChatResponseDto;
import OneCoin.Server.chat.entity.ChatMessage;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatMapper {
    ChatMessage requestDtoToChatMessage(ChatRequestDto chatRequestDto);

    ChatResponseDto chatMessageToResponseDto(ChatMessage chatMessage);

    List<ChatResponseDto> chatMessagesToResponseDtos(List<ChatMessage> chatMessage);

}
