import React from 'react';
import { ChatComponent } from './style';

interface Props {
	my: boolean;
}

function Chat({ my }: Props) {
	return (
		<ChatComponent my={my}>
			<div className="chat-content">
				{my ? <></> : <div className="chat-user">{'코린이'}</div>}
				{my ? (
					<div className="chat-message">
						<div className="time">15:09</div>
						<div className="message">{`안녕하세요!안녕하세요!안녕하세요!안녕하세요!안녕하세요!안녕하세요!안녕하세요!안녕하세요!안녕하세요!안녕하세요!안녕하세요!안녕하세요!안녕하세요!안녕하세요!`}</div>
					</div>
				) : (
					<div className="chat-message">
						<div className="message">{`안녕하세요!안녕하세요!안녕하세요!안녕하세요!안녕하세요!안녕하세요!안녕하세요!안녕하세요!안녕하세요!안녕하세요!안녕하세요!안녕하세요!안녕하세요!안녕하세요!`}</div>
						<div className="time">15:09</div>
					</div>
				)}
			</div>
		</ChatComponent>
	);
}

export default Chat;
