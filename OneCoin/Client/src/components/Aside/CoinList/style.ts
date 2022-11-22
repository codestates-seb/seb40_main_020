import styled from 'styled-components';

export const CoinListComponent = styled.div`
	width: 100%;
	height: 100%;
	.aside-title {
		display: flex;
		border-bottom: 1px solid var(--borderColor);
		> div {
			flex: 1;
			height: 45px;
			display: flex;
			justify-content: center;
			align-items: center;
			cursor: pointer;
		}
		.select {
			color: var(--yellow);
			border-bottom: 3px solid var(--yellow);
		}
	}

	.coin-search {
		width: 100%;
		height: 47px;
		border-bottom: 1px solid var(--borderColor);
		display: flex;
		input {
			box-sizing: border-box;
			width: 100%;
			height: 100%;
			border: none;
			padding-left: 1rem;
			::placeholder {
				color: var(--lightgray);
			}
		}
		.serach-icon {
			width: 45px;
			height: 100%;
			font-size: 1.5rem;
			display: flex;
			justify-content: center;
			align-items: center;
			cursor: pointer;
			color: var(--yellow);
		}
	}
	table {
		width: 100%;
		thead,
		tbody {
			> tr {
				width: 100%;
				display: flex;
				height: 35px;
				border-bottom: 1px solid var(--borderColor);
				cursor: pointer;
				:not(.sub-title) {
					height: 54px;
				}
				> td {
					display: flex;
					justify-content: center;
					align-items: center;
					flex: 1;
				}
				> tr {
					display: flex;
					flex-direction: column;
					flex: 1;
					justify-content: center;
					align-items: center;
				}
			}
		}
		.fall {
			color: var(--blue);
		}
		.rise {
			color: var(--red);
		}
	}
`;
