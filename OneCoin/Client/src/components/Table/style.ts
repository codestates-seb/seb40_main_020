import styled, { css } from 'styled-components';

const rowHeight = css`
	height: 50px;
	line-height: 50px;
	border-bottom: 1px solid var(--borderColor);
`;

export const Wrapper = styled.table`
	width: 100%;
	caption {
		margin: 35px 0 10px 5px;
		text-align: left;
	}
`;

export const Header = styled.thead`
	${rowHeight}
	border-top: 1px solid var(--borderColor);
`;

export const Body = styled.tbody`
	text-align: center;
	td {
		${rowHeight}
	}
`;
