import styled from 'styled-components';
import { contentBox } from '../../styles/index';

export const Wrapper = styled.div`
	display: flex;
	> div {
		width: 50%;
		${contentBox}
		:last-child {
			margin-left: 30px;
		}
	}
`;
