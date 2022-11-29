import styled from 'styled-components';
interface Props {
	my: boolean;
}

export const ChatComponent = styled.div<Props>`
	.chat-content {
		padding: 0.75rem;
		.chat-user {
		}
		.chat-message {
			display: flex;
			align-items: end;
			justify-content: ${({ my }) => (my ? 'end' : 'start')};
			.message {
				margin-top: 0.25rem;
				display: inline-block;
				background: ${({ my }) => (my ? '#FFEC3E' : 'var(--contentBg)')};
				line-height: 20px;
				border-radius: 15px;
				padding: 0.5rem;
				max-width: 266px;
			}
			.time {
				margin: 0 0.35rem;
				display: inline-block;
				color: #333;
				font-size: 0.85rem;
			}
		}
		.system-message {
			display: flex;
			justify-content: center;
		}
	}
`;
