import styled, { css } from 'styled-components';

const rowHeight = css`
	height: 30px;
	padding: 10px;
	vertical-align: middle;
	border-bottom: 1px solid var(--borderColor);
	line-height: normal;
`;

export const Wrapper = styled.table`
	width: 100%;
	margin-top: 30px;
	caption {
		margin: 5px 0 10px 5px;
		text-align: left;
	}
`;

export const Header = styled.thead`
	th {
		${rowHeight}
	}
	border-top: 1px solid var(--borderColor);
`;

export const Body = styled.tbody`
	text-align: center;
	td {
		${rowHeight}
	}
	.none-data {
		display: block;
		width: 100%;
		margin: 50px auto;
	}
`;
