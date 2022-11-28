import React from 'react';
import { ChatComponent } from './style';
import { ChatData } from '../../../../utills/types';

interface Props {
	my: boolean;
	data: ChatData;
}

function Chat({ my, data }: Props) {
	const { userDisplayName, message, chatAt } = data;
	return (
		<ChatComponent my={my}>
			<div className="chat-content">
				{my ? <></> : <div className="chat-user">{userDisplayName}</div>}
				{my ? (
					<div className="chat-message">
						<div className="time">15:09</div>
						<div className="message">{message}</div>
					</div>
				) : (
					<div className="chat-message">
						<div className="message">{message}</div>
						<div className="time">15:09</div>
					</div>
				)}
			</div>
		</ChatComponent>
	);
}

export default Chat;
