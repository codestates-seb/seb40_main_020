import styled, { css } from 'styled-components';
import { flexRowBetween } from 'styles';

const rowHeight = css`
	height: 30px;
	padding: 10px;
	vertical-align: middle;
	border-bottom: 1px solid var(--borderColor);
	line-height: normal;
`;

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
	table {
		width: 100%;
		margin-top: 30px;
		thead {
			th {
				${rowHeight}
			}
			border-top: 1px solid var(--borderColor);
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
	}
`;
