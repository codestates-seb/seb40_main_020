import styled from 'styled-components';

interface Props {
	todayChange?: string;
}

export const ExchangeComponent = styled.div<Props>`
	display: grid;
	gap: 8px;
	grid-template-columns: 495px 495px 400px;
	grid-template-rows: 210px 450px 460px 340px 220px 220px;
	.today-range {
		color: ${({ todayChange }) =>
			todayChange === 'RISE'
				? 'var(--red)'
				: todayChange === 'FALL'
				? 'var(--blue)'
				: '#000'};
	}
	.coin-title {
		width: 100%;
		height: 100%;
		box-sizing: border-box;
		background: #fff;
		grid-column: 1/3;
		grid-row: 1/2;
		border: 1px solid var(--borderColor);
		h1 {
			font-size: 32px;
			font-weight: 700;
			height: 77px;
			border-bottom: 1px solid var(--borderColor);
			display: flex;
			align-items: center;
			padding-left: 1rem;
		}
		> div {
			height: 130px;
			display: flex;
			flex-direction: column;
			justify-content: center;
			padding-left: 1rem;
			h2 {
				font-size: 32px;
				font-weight: 700;
				margin-top: -0.25rem;
				margin-bottom: 0.25rem;
			}
		}
	}
	.chart-wrapper {
		grid-column: 1/3;
		grid-row: 2/3;
	}
	.quote-wrapper {
		grid-column: 1/2;
		grid-row: 3/5;
	}
	.order-wrapper {
		grid-column: 2/3;
		grid-row: 3/4;
	}
	.hold-wrapper {
		grid-column: 2/4;
		grid-row: 4/5;
	}
	.aside-wrapper {
		grid-column: 3/4;
		grid-row: 1/4;
	}
	.today-price {
		font-size: 16px;
		span {
			:nth-child(1) {
				padding-left: 0;
			}
			padding: 0 0.25rem;
		}
	}
	background: var(--backgroundColor);
`;
