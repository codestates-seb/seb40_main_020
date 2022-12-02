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
		width: 15%;
		:nth-child(1) {
			width: 100px;
		}
		:nth-child(2),
		:nth-child(3) {
			width: 70px;
		}
	}
	table {
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
		.none-data {
			display: block;
			width: 100%;
			margin: 50px auto;
		}
	}
`;
