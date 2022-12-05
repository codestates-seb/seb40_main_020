import styled, { css } from 'styled-components';
import { contentBox } from '../../styles/index';

const rowHeight = css`
	height: 30px;
	padding: 10px;
	vertical-align: middle;
	border-bottom: 1px solid var(--borderColor);
	line-height: normal;
`;
export const Wrapper = styled.div`
	${contentBox}
	table thead th {
		:nth-child(1) {
			width: 150px;
		}
		:nth-child(4) {
			width: 100px;
		}
		:nth-child(2) {
			width: 100px;
		}
		:nth-child(3) {
			width: 120px;
		}
	}
	table {
		margin-top: 30px;
		width: 100%;
		thead {
			th {
				${rowHeight}
			}
			border-top: 1px solid var(--borderColor);
		}
	}
	tbody {
		text-align: center;
		td {
			${rowHeight}
		}
	}
	.ask {
		color: var(--blue);
	}
	.bid {
		color: var(--red);
	}
`;
