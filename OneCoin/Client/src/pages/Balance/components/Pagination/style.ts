import styled from 'styled-components';
import { flexCenter } from '../../../../styles/index';

export const Wrapper = styled.div`
	ul {
		${flexCenter}
		padding:3rem 0 10rem 0;
		li {
			padding: 5px 10px;
			margin: 0 3px;
			cursor: pointer;
			&.disabled {
				color: var(--borderColor);
			}
		}
		.selected {
			box-shadow: 0 0 2px var(--yellow);
			border: 1px solid var(--yellow);
			font-weight: 700;
			color: var(--yellow);
		}
		.break {
			margin: 0 8px;
		}
	}
`;
