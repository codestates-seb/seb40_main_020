import React from 'react';
import { ChatComponent } from './style';
import { ChatData } from '../../../../utills/types';

interface Props {
	data: ChatData;
	userId: number;
}

function Chat({ data, userId }: Props) {
	const { userDisplayName, message, chatAt, type } = data;
	const date = new Date(chatAt);
	const hours = date.getHours();
	const minutes = date.getMinutes();
	const time = `${hours >= 10 ? hours : `0${hours}`}:${
		minutes >= 10 ? minutes : `0${minutes}`
	}`;
	const me = data.userId === userId;
	return (
		<ChatComponent my={me}>
			<div className="chat-content">
				{type === 'ENTER' || type === 'LEAVE' ? (
					<></>
				) : me ? (
					<></>
				) : (
					<div className="chat-user">{userDisplayName}</div>
				)}
				{type === 'ENTER' || type === 'LEAVE' ? (
					<div className="system-message">{message}</div>
				) : me ? (
					<div className="chat-message">
						<div className="time">{time}</div>
						<div className="message">{message}</div>
					</div>
				) : (
					<div className="chat-message">
						<div className="message">{message}</div>
						<div className="time">{time}</div>
					</div>
				)}
				{}
			</div>
		</ChatComponent>
	);
}

export default Chat;
