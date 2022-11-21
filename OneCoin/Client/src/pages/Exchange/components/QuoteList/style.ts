import styled from 'styled-components';

export const QuoteListComponent = styled.div`
	width: 100%;
	height: 100%;
	overflow: auto;
	border: 1px solid var(--borderColor);
	.normal {
		height: 45px;
		color: var(--yellow);
		font-size: 24px;
		font-weight: 700;
		display: flex;
		align-items: center;
		padding-left: 1rem;
	}
	.ask {
		> div {
			background: #eef2fb;
		}
		color: var(--blue);
	}
	.bid {
		> div {
			background: #fcf0ef;
		}
		color: var(--red);
	}
	background: #fff;
`;
