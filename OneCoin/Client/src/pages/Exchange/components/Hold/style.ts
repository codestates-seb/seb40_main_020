import styled from 'styled-components';

export const HoldComponent = styled.div`
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
					flex-direction: column;
					justify-content: center;
					align-items: center;
					flex: 1;
				}
			}
		}
	}
	.trade-status {
		display: flex;
		td {
			display: flex;
			flex-direction: row;
			justify-content: start;
			margin-left: 1rem;
			> div {
				margin: 0.25rem 1rem;
			}
		}
	}
	.hold-item {
		height: 50px;

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
	.ask {
		color: var(--blue);
	}
	.bid {
		color: var(--red);
	}
`;
