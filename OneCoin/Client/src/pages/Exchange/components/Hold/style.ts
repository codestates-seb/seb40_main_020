import styled from 'styled-components';

export const HoldComponent = styled.div`
	width: 900px;
	height: 322px;
	border: 1px solid var(--borderColor);
	margin-top: 12px;
	background: #fff;
	.hold-menu {
		width: 100%;
		height: 45px;
		box-sizing: border-box;
		border-bottom: 1px solid var(--borderColor);
		display: flex;

		> div {
			flex: 1;
			display: flex;
			justify-content: center;
			align-items: center;
			font-weight: 700;
		}
		.select {
			color: var(--yellow);
			border-bottom: 3px solid var(--yellow);
		}
	}

	table {
		width: 100%;
		tbody,
		thead {
			> tr {
				width: 100%;
				display: flex;
				height: 35px;
				border-bottom: 1px solid var(--borderColor);
				> td {
					display: flex;
					justify-content: center;
					align-items: center;
					flex: 1;
				}
			}
		}
	}

	.hold-item {
		height: 50px;
		.ask {
			color: var(--blue);
		}
		.bid {
			color: var(--red);
		}
		.cancel {
			cursor: pointer;
		}
		> tr {
			display: flex;
			flex-direction: column;
			flex: 1;
			justify-content: center;
			align-items: center;
		}
	}
`;
