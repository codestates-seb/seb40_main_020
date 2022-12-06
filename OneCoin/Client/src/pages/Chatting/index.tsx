import React, { useState, useEffect } from 'react';
import { ChattingComponent } from './style';
import Toggle from './components/Toggle';
import ChatBox from './components/ChatBox';
import { useLocation } from 'react-router-dom';
import { exitClient } from 'api/socket';
import { useRecoilState } from 'recoil';
import { sessionIdState } from 'store';
const Chatting = () => {
	const [isChat, setIsChat] = useState(false);
	const [sessionId, setSessionId] = useRecoilState(sessionIdState);
	const chatClickHandler = () => setIsChat(!isChat);
	const { pathname } = useLocation();
	const log = ['/login', '/signup', '/findpassword'].includes(pathname);
	useEffect(() => {
		if (isChat) chatClickHandler();
		if (log) {
			exitClient();
			setSessionId('');
		}
	}, [pathname]);
	return (
		<ChattingComponent log={log}>
			{isChat ? (
				<ChatBox chatClickHandler={chatClickHandler} isChat={isChat} />
			) : (
				<Toggle chatClickHandler={chatClickHandler} />
			)}
		</ChattingComponent>
	);
};
export default Chatting;
