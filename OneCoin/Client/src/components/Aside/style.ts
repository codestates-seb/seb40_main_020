import styled from 'styled-components';
import { contentBox } from '../../styles/index';

export const AsideComponent = styled.aside<{ isShow?: boolean }>`
	display: ${(props) => !props.isShow && 'none'};
	min-width: 400px;
	height: 100%;
	background: #fff;
	${contentBox}
`;
