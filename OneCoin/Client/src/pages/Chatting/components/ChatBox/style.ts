import styled from 'styled-components';

export const ChatBoxComponent = styled.div`
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
		font-size: 24px;
		padding: 0.25rem 1rem;
		svg {
			cursor: pointer;
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
			padding: 0.35rem;
			border: none;
			border-radius: 15px;
			background: #dddddd;
		}
	}
	.chat-body {
		overflow: auto;
		overflow: auto;
		::-webkit-scrollbar {
			width: 10px;
		}
		::-webkit-scrollbar-thumb {
			border-radius: 8px;
			background-color: #d9d9d9;
		}
	}
`;
