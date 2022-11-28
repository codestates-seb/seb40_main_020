import React, { useEffect, useRef, useState } from 'react';
import { ChatBoxComponent } from './style';
import logo from '../../../../assets/images/one.png';
import { IoCloseOutline } from 'react-icons/io5';
import Chat from '../Chat';
import { ChatData } from '../../../../utills/types';
import { getChatHistory } from '../../../../api/chat';

interface Props {
	chatClickHandler: () => void;
}

function ChatBox({ chatClickHandler }: Props) {
	const scrollRef = useRef<null | HTMLDivElement>(null);
	const [msgData, setMsgData] = useState<ChatData[]>([]);
	useEffect(() => {
		scrollRef?.current?.scrollIntoView(false);
	}, [scrollRef]);
	const getChatHistoryMsg = async () => {
		const res = await getChatHistory();
		console.log(res);
		setMsgData([...res]);
	};
	useEffect(() => {
		getChatHistoryMsg();
	}, []);

	const arr = [0, 1, 2, 3, 4, 5];
	return (
		<ChatBoxComponent>
			<header className="chat-header">
				<img src={logo} />
				<IoCloseOutline onClick={chatClickHandler} />
			</header>
			<div className="chat-body">
				{msgData.length &&
					msgData.map((v, i) => <Chat my={false} key={i} data={v} />)}
				<div ref={scrollRef}></div>
			</div>

			<form
				onClick={(e) => {
					e.preventDefault();
					console.log('ㅁㄴ');
				}}
			>
				<input type="text" />
			</form>
		</ChatBoxComponent>
	);
}

export default ChatBox;
