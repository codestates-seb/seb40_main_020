import styled from 'styled-components';

export const ChatBoxComponent = styled.div`
	z-index: 1;
	width: 370px;
	height: 620px;
	border-radius: 20px;
	position: fixed;
	bottom: 20px;
	right: 10px;
	background: var(--yellow);
	.chat-header {
		display: flex;
		border-bottom: 1px solid var(--borderColor);
		justify-content: space-between;
		align-items: center;
		.select {
			background: #2ebde859;
		}
		padding: 0.25rem 1rem;
		> div {
			display: flex;
			justify-content: center;
			align-items: center;
			.chat-user {
				margin-right: 1rem;
			}
		}
		.close-icon {
			font-size: 24px;
			svg {
				cursor: pointer;
			}
		}

		img {
			width: 80px;
			height: 60px;
		}
	}
	> div {
		height: 495px;
		border-bottom: 1px solid var(--borderColor);
	}

	form {
		display: flex;
		justify-content: center;
		align-items: center;
		height: 50px;
		/* height: 100%; */
		input {
			width: 340px;
			box-sizing: border-box;
			padding: 0.5rem;
			padding-left: 1rem;
			border: none;
			border-radius: 15px;
			background: #fff;
		}
	}
	.chat-body {
		overflow: auto;
		::-webkit-scrollbar {
			width: 10px;
		}
		::-webkit-scrollbar-thumb {
			border-radius: 8px;
			background-color: #d9d9d9;
		}
	}
	.chat-rooms {
		display: flex;
		> div {
			display: flex;
			justify-content: center;
			align-items: center;
			background: #fff;
			border-radius: 50%;
			cursor: pointer;
			width: 28px;
			height: 28px;
			font-size: 12px;
			margin: 0 0.125rem;
		}
	}
	.need-login {
		display: flex;
		justify-content: center;
		margin-top: 1rem;
		/* align-items: center; */
	}
`;
