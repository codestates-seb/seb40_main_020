import styled, { css } from 'styled-components';

export const spanTitle = css`
	display: block;
	margin-bottom: 5px;
`;

export const Wrapper = styled.div`
	flex: 1;
	span {
		${spanTitle}
	}
	ul {
		display: flex;
		button {
			border: 1px solid var(--borderColor);
			background: #fff;
			padding: 5px 15px;
			height: 37px;
			&.active {
				border: 1px solid var(--yellow);
				color: var(--yellow);
				font-weight: 700;
			}
		}
	}
`;
