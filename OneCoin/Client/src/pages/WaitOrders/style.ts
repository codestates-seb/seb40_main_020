import styled from 'styled-components';
import { contentBox } from '../../styles/index';

export const Wrapper = styled.div`
	${contentBox}
	table thead th {
		width: 15%;
		:nth-child(1) {
			width: 100px;
		}
		:nth-child(2),
		:nth-child(3) {
			width: 70px;
		}
	}
`;
