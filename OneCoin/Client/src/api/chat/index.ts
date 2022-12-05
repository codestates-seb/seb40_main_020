import StompJs from 'stompjs';
import SockJS from 'sockjs-client';
import { ChatMsg, ChatData, RoomsInfo } from '../../utills/types';
import api from '..';

type AddMsg = (chatData: ChatData[]) => void;
type SetRoomsInfo = React.Dispatch<React.SetStateAction<RoomsInfo[]>>;
type SetUserId = React.Dispatch<React.SetStateAction<number | null>>;

let client: StompJs.Client;
let subscriptions: StompJs.Subscription;
export const enterRoom = (
	room: number,
	addMsgData: AddMsg,
	setRoomsInfo: SetRoomsInfo,
	setUserId: SetUserId
) => {
	const sock = new SockJS(`${process.env.REACT_APP_SERVER_URL}/ws/chat`);
	client = StompJs.over(sock);
	client.debug = function () {
		return;
	};
	const Authorization = localStorage.getItem('login-token') as string;
	const headers = { Authorization };
	client.connect(headers, (frame) => {
		const headers = frame?.headers;
		if (headers) {
			const userName = Object.values(headers)[0] as string;
			const user = userName.split(', ')[2];
			if (user) {
				const userId = user.replace('id=', '');
				setUserId(Number(userId));
			}
		}

		setTimeout(() => {
			subscribeRoom(room, addMsgData);
			subscribeRoomsInfo(setRoomsInfo);
		}, 500);
	});
};

const subscribeRoomsInfo = (setUsers: SetRoomsInfo) => {
	client.subscribe('/topic/rooms-info', (msg) => {
		const data = JSON.parse(msg.body);
		setUsers(data);
	});
};
const subscribeRoom = (room: number, addMsgData: AddMsg) => {
	subscriptions = client.subscribe(`/topic/rooms/${room}`, (msg) => {
		const newMsg = JSON.parse(msg.body);
		addMsgData([newMsg]);
	});
};

export const changeRoom = (room: number, ch: number, addMsgData: AddMsg) => {
	subscriptions.unsubscribe();
	subscribeRoom(ch, addMsgData);
};

export const exitChat = () => {
	client.disconnect(() => {
		console.log('exit');
	});
};

export const sendMsg = (data: ChatMsg) => {
	client.send('/app/rooms', {}, JSON.stringify(data));
};

export const getChatHistory = async (room: number) => {
	try {
		const res = await api.get(`/ws/chat/rooms/${room}/messages`);
		return res.data.data;
	} catch (err) {
		console.log(err);
	}
};
