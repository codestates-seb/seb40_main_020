import styled from 'styled-components';

interface Props {
	chartSelector: number;
}

export const ChartListComponent = styled.div<Props>`
	height: 100%;
	.min-wrapper {
		display: flex;
		justify-content: ${({ chartSelector }) =>
			chartSelector === 1 ? 'space-between' : 'end'};
		.m {
			cursor: pointer;
		}
		> div {
			display: flex;
			flex-direction: row;
			justify-content: center;
			align-items: center;
			> div {
				padding: 0.25rem;
				font-size: 12px;
			}
		}
	}
	.select {
		background: var(--yellow);
	}
`;
