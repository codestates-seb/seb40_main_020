import StompJs from 'stompjs';
import SockJS from 'sockjs-client';
import {
	Ticker,
	OrderBook,
	CoinDataType,
	ChatMsg,
	ChatData,
	RoomsInfo,
} from 'utills/types';
import api from './index';

type T = (
	coinData: CoinDataType[],
	setCoinData: React.Dispatch<React.SetStateAction<CoinDataType[]>>
) => void;

type AddMsg = (chatData: ChatData[]) => void;
type SetRoomsInfo = React.Dispatch<React.SetStateAction<RoomsInfo[]>>;
type SetUserId = React.Dispatch<React.SetStateAction<number | null>>;
type SetSessionId = React.Dispatch<React.SetStateAction<string>>;
type SetMsgData = React.Dispatch<React.SetStateAction<ChatData[]>>;

type Connect = (setSessionId: SetSessionId, setUserId: SetUserId) => void;
type GetHistory = (
	room: number,
	sessionId: string,
	setMsgData: SetMsgData
) => void;
type EnterRoom = (
	room: number,
	addMsgData: AddMsg,
	setRoomsInfo: SetRoomsInfo
) => void;

let client: StompJs.Client;
let chatSub: StompJs.Subscription;
let chatInfoSub: StompJs.Subscription;

export const connect: Connect = (setSessionId, setUserId) => {
	const socket = new SockJS(
		`${process.env.REACT_APP_SERVER_URL}/ws/upbit-info`
	);
	client = StompJs.over(socket);
	client.debug = function () {
		return;
	};
	const Authorization = localStorage.getItem('login-token') as string;
	const headers = { Authorization };
	client.connect(headers, (frame) => {
		const transport = Object.values(socket)[21].url;
		const sessionId = transport.split('/')[6];
		setSessionId(sessionId);
		const headers = frame?.headers;
		if (headers) {
			const userName = Object.values(headers)[0] as string;
			const user = userName.split(', ')[2];
			if (user) {
				const userId = user.replace('id=', '');
				setUserId(Number(userId));
			}
		}
	});
};

export const enterRoom: EnterRoom = async (room, addMsgData, setRoomsInfo) => {
	try {
		await subscribeRoom(room, addMsgData);
		await subscribeRoomsInfo(setRoomsInfo);
	} catch (err) {
		console.log(err);
	}
};

export const subscribeRoomsInfo = (setUsers: SetRoomsInfo) => {
	chatInfoSub = client.subscribe('/topic/rooms-info', (msg) => {
		const data = JSON.parse(msg.body);
		setUsers(data);
	});
};
export const subscribeRoom = (room: number, addMsgData: AddMsg) => {
	chatSub = client.subscribe(`/topic/rooms/${room}`, (msg) => {
		const newMsg = JSON.parse(msg.body);
		addMsgData([newMsg]);
	});
};
export const unSubscribeRoom = () => {
	chatSub.unsubscribe();
	chatInfoSub.unsubscribe();
};

export const getChatHistory: GetHistory = async (
	room,
	sessionId,
	setMsgData
) => {
	try {
		const res = await api.get(
			`/ws/chat/rooms/${room}/messages?sessionId=${sessionId}`
		);
		const data = res.data.data;
		if (data) {
			setMsgData([...data].reverse());
		}
	} catch (err) {
		console.log(err);
	}
};

export const sendMsg = (data: ChatMsg) => {
	client.send('/app/rooms', {}, JSON.stringify(data));
};

export const changeRoom = async (ch: number, addMsgData: AddMsg) => {
	await chatSub.unsubscribe();
	await subscribeRoom(ch, addMsgData);
};

export const exitClient = () => {
	client.disconnect(() => {
		console.log('exit');
	});
};
export const coinDataSubscribe: T = (coinData, setCoinData) => {
	client.subscribe('/info/upbit', (msg) => {
		const data = JSON.parse(msg.body).data;
		const newCoinData = [...coinData].map((v) => {
			const ticker = data.ticker.filter(
				(tic: Ticker) => tic.code === v.code
			)[0];
			const orderBook = data.orderBook.filter(
				(order: OrderBook) => order.code === v.code
			)[0];
			const obj = { ...v, ticker, orderBook };
			return obj;
		});
		setCoinData(newCoinData);
	});
};
