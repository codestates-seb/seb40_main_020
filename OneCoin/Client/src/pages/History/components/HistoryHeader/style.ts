import styled from 'styled-components';
import { spanTitle } from '../ButtonList/style';

export const Wrapper = styled.div`
	display: flex;
	width: 100%;
	margin: 50px 0 30px;
	padding: 0 30px;
	box-sizing: border-box;
`;

export const SearchBox = styled.div`
	flex: 1;
	span {
		${spanTitle}
	}
	> div {
		position: relative;
		width: 100%;
		input {
			width: 100%;
			border: 1px solid var(--borderColor);
			padding: 10px 30px 10px 10px;
			box-sizing: border-box;
		}
		.icon {
			position: absolute;
			top: 50%;
			right: 0;
			transform: translate(-50%, -50%);
			cursor: pointer;
		}
	}
`;
