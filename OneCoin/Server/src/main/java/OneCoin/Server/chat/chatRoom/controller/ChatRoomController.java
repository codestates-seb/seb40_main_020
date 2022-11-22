package OneCoin.Server.chat.chatRoom.controller;

import OneCoin.Server.chat.chatRoom.dto.ChatRoomDto;
import OneCoin.Server.chat.chatRoom.entity.ChatRoom;
import OneCoin.Server.chat.chatRoom.mapper.ChatRoomMapper;
import OneCoin.Server.chat.chatRoom.service.ChatRoomService;
import OneCoin.Server.dto.MultiResponseDto;
import OneCoin.Server.dto.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ws/chat/rooms")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final ChatRoomMapper chatRoomMapper;

    //방 생성
    @PostMapping
    public ResponseEntity createRoom(@RequestParam String name,
                                     @RequestParam String nation) {
        ChatRoom chatRoom = chatRoomService.createRoom(name, nation);
        ChatRoomDto dto = chatRoomMapper.chatRoomToChatRoomDto(chatRoom);
        //채팅 방에 몇 명이 있는지 조회하는 기능 추가
        return new ResponseEntity<>(new SingleResponseDto<>(dto), HttpStatus.CREATED);
    }

    //채팅방 리스트 국가 별 조회
    @GetMapping
    public ResponseEntity findByNation(@RequestParam String nation) {
        List<ChatRoom> rooms = chatRoomService.findRoomByNation(nation);
        List<ChatRoomDto> dtos = chatRoomMapper.chatRoomListToChatRoomDtoList(rooms);
        return new ResponseEntity<>(new MultiResponseDto<>(dtos), HttpStatus.OK);
    }
}
