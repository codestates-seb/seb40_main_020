import axios from 'axios';
import StompJs from 'stompjs';
import SockJS from 'sockjs-client';
import { SERVER_URL } from '../';
import { ChatMsg } from '../../utills/types';

const sock = new SockJS(`${SERVER_URL}/ws/chat`);
const client = StompJs.over(sock);
export const enterRoom = (Authorization: string, room: number) => {
	client.connect({ Authorization }, (frame) => {
		console.log(frame);
		setTimeout(() => {
			client.subscribe(`/topic/rooms/${room}`, (msg) => {
				console.log('msg', msg);
			});
			client.subscribe('/topic/rooms-info', (msg) => {
				console.log(msg, 'room');
			});
		}, 500);
	});
};

export const exitRoom = (room: number) => {
	client.unsubscribe(`/topic/rooms/${room}`);
};

// export const exitChat = () => {
// 	client.disconnect();
// };

export const sendMsg = (data: ChatMsg) => {
	client.send('/app/rooms', {}, JSON.stringify(data));
};

export const getChatHistory = async () => {
	try {
		const res = await axios.get(`${SERVER_URL}/ws/chat/rooms/1/messages`);
		return res.data.data;
	} catch (err) {
		console.log(err);
	}
};
