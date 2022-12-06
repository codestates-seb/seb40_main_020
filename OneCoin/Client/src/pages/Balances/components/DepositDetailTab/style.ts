import styled, { css } from 'styled-components';
const rowHeight = css`
	height: 30px;
	padding: 10px;
	vertical-align: middle;
	border-bottom: 1px solid var(--borderColor);
	line-height: normal;
`;
export const Wrapper = styled.div`
	table {
		margin-top: -1px;
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
`;
