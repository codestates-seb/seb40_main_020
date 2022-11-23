import React, { useState } from 'react';
import { ChattingComponent } from './style';
import Toggle from './components/Toggle';
import ChatBox from './components/ChatBox';
const Chatting = () => {
	const [isChat, setIsChat] = useState(false);
	const chatClickHandler = () => setIsChat(!isChat);
	return (
		<ChattingComponent>
			{isChat ? (
				<ChatBox chatClickHandler={chatClickHandler} />
			) : (
				<Toggle chatClickHandler={chatClickHandler} />
			)}
		</ChattingComponent>
	);
};
export default Chatting;
