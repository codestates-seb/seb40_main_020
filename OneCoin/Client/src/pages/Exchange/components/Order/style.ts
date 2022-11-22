import styled from 'styled-components';

export const OrderComponent = styled.div`
	width: 100%;
	height: 100%;
	background: #fff;
	.order-menu {
		display: flex;
		width: 100%;
		height: 45px;
		border-bottom: 1px solid var(--borderColor);
		cursor: pointer;
		.menu-item {
			margin-right: 1rem;
			flex: 1;
			display: flex;
			align-items: center;
			justify-content: center;
		}
		.select {
			color: var(--yellow);
		}
	}
	.order-contents {
		margin-top: 55px;
		> div {
			display: flex;
			/* justify-content: space-between; */
			/* margin: 0 1rem; */
		}
		.order {
			margin: 0 1rem;
			margin-bottom: 1.75rem;
			display: flex;
			justify-content: space-between;

			align-items: center;
			:nth-child(3) {
				align-items: baseline;
			}
			.order-title {
				font-size: 16px;
			}
			.order-input {
				display: flex;
				width: 328px;
				height: 38px;

				box-sizing: border-box;
				button {
					width: 50px;
					height: 100%;
					cursor: pointer;
					border: none;
					border-left: 1px solid var(--borderColor);
				}
				input {
					box-sizing: border-box;
					width: 100%;
					height: 100%;
					padding: 0;
					border: none;
					padding: 0.5rem;
					text-align: right;
					border: 1px solid var(--borderColor);
				}
			}
		}
	}
	.order-size {
		.size-btn-wrapper {
			display: flex;
			width: 100%;
			margin-top: 0.5rem;
			.select-btn {
				margin-top: 0.5rem;
				font-size: 14px;
				box-sizing: border-box;
				display: flex;
				justify-content: center;
				align-items: center;
				width: 60px;
				height: 25px;
				border: 1px solid var(--borderColor);
				margin: 0 auto 0 0;
				cursor: pointer;
				:nth-last-child(1) {
					margin: 0;
				}
			}
		}
	}
	.order-btn {
		display: flex;
		justify-content: center;
	}
`;
