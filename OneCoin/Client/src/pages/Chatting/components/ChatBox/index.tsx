import React, { useEffect, useRef, useState, memo } from 'react';
import { ChatBoxComponent } from './style';
import logo from '../../../../assets/images/one.png';
import { IoCloseOutline } from 'react-icons/io5';
import Chat from '../Chat';
import { ChatData, RoomsInfo } from '../../../../utills/types';
import {
	getChatHistory,
	enterRoom,
	sendMsg,
	changeRoom,
	exitChat,
} from '../../../../api/chat';

interface Props {
	chatClickHandler: () => void;
}

function ChatBox({ chatClickHandler }: Props) {
	const scrollRef = useRef<null | HTMLDivElement>(null);
	const [msgData, setMsgData] = useState<ChatData[]>([]);
	const [roomsInfo, setRoomsInfo] = useState<RoomsInfo[]>([]);
	const [userId, setUserId] = useState<number | null>(null);
	const [users, setUsers] = useState(0);
	const [msg, setMsg] = useState('');
	const [room, setRoom] = useState(1);
	const rooms = [1, 2];
	const Authorization = sessionStorage.getItem('login-token') as string;
	useEffect(() => {
		getChat(room);
		enterRoom(Authorization, room, addMsgData, setRoomsInfo, setUserId);
	}, [Authorization]);
	useEffect(() => {
		scrollRef?.current?.scrollIntoView(false);
	}, [msgData]);
	useEffect(() => {
		if (roomsInfo.length) {
			const data = roomsInfo.filter((v) => v.chatRoomId === room)[0];
			setUsers(data.numberOfChatters);
		}
	}, [roomsInfo]);

	const addMsgData = (chatData: ChatData[]) => {
		setMsgData((msgData) => [...msgData, ...chatData]);
	};

	const getChat = async (ch: number) => {
		const res = await getChatHistory(ch);
		setMsgData([...res].reverse());
	};

	const msgOnChangeHandler = (e: React.ChangeEvent<HTMLInputElement>) => {
		setMsg(e.target.value);
	};
	const msgSubmitHandler = (e: React.FormEvent<HTMLFormElement>) => {
		e.preventDefault();
		const newMsg = {
			type: 'TALK',
			chatRoomId: room,
			message: msg,
		};
		setMsg('');
		sendMsg(newMsg);
	};

	const roomsChangeHandler = (ch: number) => {
		setRoom(ch);
		getChat(ch);
		changeRoom(room, ch, addMsgData);
	};
	const closeBtnHandler = () => {
		exitChat();
		chatClickHandler();
	};
	return (
		<ChatBoxComponent>
			<header className="chat-header">
				<img src={logo} />
				<div>
					<div className="chat-rooms">
						{rooms.map((v, i) => (
							<div
								key={i}
								onClick={() => roomsChangeHandler(v)}
								className={room === v ? 'select' : ''}
							>{`ch.${v}`}</div>
						))}
					</div>
					<div className="chat-user">
						<span>참여자</span>
						<span>{users}</span>
					</div>
					<div className="close-icon">
						<IoCloseOutline onClick={closeBtnHandler} />
					</div>
				</div>
			</header>
			<div className="chat-body">
				{msgData.length &&
					msgData.map((v, i) => (
						<Chat key={i} data={v} userId={userId as number} />
					))}
				<div ref={scrollRef}></div>
			</div>
			{Authorization ? (
				<form onSubmit={msgSubmitHandler}>
					<input type="text" onChange={msgOnChangeHandler} value={msg} />
				</form>
			) : (
				<div className="need-login">로그인 이후 채팅이 가능합니다.</div>
			)}
		</ChatBoxComponent>
	);
}

export default memo(ChatBox);
