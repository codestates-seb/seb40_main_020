import React from 'react';
import { ChatBoxComponent } from './style';
import logo from '../../../../assets/images/one.png';
import { IoCloseOutline } from 'react-icons/io5';
import Chat from '../Chat';

interface Props {
	chatClickHandler: () => void;
}

function ChatBox({ chatClickHandler }: Props) {
	return (
		<ChatBoxComponent>
			<header className="chat-header">
				<img src={logo} />
				<IoCloseOutline onClick={chatClickHandler} />
			</header>
			{/* <ChatRoom /> */}
			<div>
				<Chat my={false} />
				<Chat my={true} />
			</div>

			<form
				onSubmit={(e) => {
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
