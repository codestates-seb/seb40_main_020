import styled from 'styled-components';
import { flexRowBetween } from 'styles';

export const Wrapper = styled.div`
	flex: 1;
	.title {
		${flexRowBetween}
		margin:30px;
		span {
			font-size: 0.9rem;
		}
		strong {
			margin-right: 5px;
			color: var(--red);
			font-size: 1.2rem;
		}
	}
	.search-box {
		position: relative;
		width: 100%;
		margin-bottom: -31px;
		input {
			width: 100%;
			height: 47px;
			border: 0 none;
			border-top: 1px solid var(--borderColor);
			border-bottom: 1px solid var(--borderColor);
			padding: 10px 30px 10px 10px;
			box-sizing: border-box;
		}
		.icon {
			position: absolute;
			top: 50%;
			right: 0;
			transform: translate(-50%, -50%);
			cursor: pointer;
			font-size: 1.5rem;
		}
	}
`;
