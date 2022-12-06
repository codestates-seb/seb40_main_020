import styled, { css } from 'styled-components';
import { contentBox } from '../../styles/index';

const rowHeight = css`
	height: 30px;
	padding: 10px;
	vertical-align: middle;
	border-bottom: 1px solid var(--borderColor);
	line-height: normal;
`;
export const Wrapper = styled.section`
	${contentBox}
	table {
		width: 100%;
		thead {
			th {
				${rowHeight}
			}
			border-top: 1px solid var(--borderColor);
		}
	}
	.title {
		display: flex;
		height: 50px;
		align-items: center;
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
	.rise {
		color: var(--red);
	}
	.fall {
		color: var(--blue);
	}
`;
