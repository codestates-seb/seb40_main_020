import styled from 'styled-components';
import { container } from '../../styles';

export const HeaderComponent = styled.header`
	width: 100%;
	height: 80px;
	background: var(--yellow);
	display: flex;
	justify-content: center;
	.content {
		${container}
		justify-content: space-between;
		background: var(--yellow);
		img {
			width: 115px;
			height: 80px;
			cursor: pointer;
		}
		.nav {
			display: flex;
			align-items: center;
			> div {
				margin: 0.5rem 0.9rem;
				cursor: pointer;
				display: flex;
			}
		}
	}
`;
