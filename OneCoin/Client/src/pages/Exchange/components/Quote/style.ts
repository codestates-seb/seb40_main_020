import styled from 'styled-components';

interface Props {
	tradePrice?: number;
	price: number;
}

export const QuoteComponent = styled.div<Props>`
	height: 45px;
	box-sizing: border-box;
	display: flex;
	border: ${({ tradePrice, price }) =>
		tradePrice === price ? ' 3px solid var(--yellow)' : 'none'};
	align-items: center;
	padding: 1rem;
	margin-top: 2px;
	.price {
		display: flex;
		width: 365px;
		.rate {
			margin-left: 1rem;
		}
	}
`;
