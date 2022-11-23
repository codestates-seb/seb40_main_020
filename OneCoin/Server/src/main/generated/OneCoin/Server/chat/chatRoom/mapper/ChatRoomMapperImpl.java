package OneCoin.Server.chat.chatRoom.mapper;

import OneCoin.Server.chat.chatRoom.dto.ChatRoomDto;
import OneCoin.Server.chat.chatRoom.entity.ChatRoom;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-11-23T16:28:10+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 11.0.17 (Azul Systems, Inc.)"
)
@Component
public class ChatRoomMapperImpl implements ChatRoomMapper {

    @Override
    public ChatRoomDto chatRoomToChatRoomDto(ChatRoom chatRoom) {
        if ( chatRoom == null ) {
            return null;
        }

        Long chatRoomId = null;
        String name = null;
        String nation = null;

        chatRoomId = chatRoom.getChatRoomId();
        name = chatRoom.getName();
        nation = chatRoom.getNation();

        Long numberOfChatters = null;

        ChatRoomDto chatRoomDto = new ChatRoomDto( chatRoomId, name, nation, numberOfChatters );

        return chatRoomDto;
    }

    @Override
    public List<ChatRoomDto> chatRoomListToChatRoomDtoList(List<ChatRoom> chatRooms) {
        if ( chatRooms == null ) {
            return null;
        }

        List<ChatRoomDto> list = new ArrayList<ChatRoomDto>( chatRooms.size() );
        for ( ChatRoom chatRoom : chatRooms ) {
            list.add( chatRoomToChatRoomDto( chatRoom ) );
        }

        return list;
    }
}
