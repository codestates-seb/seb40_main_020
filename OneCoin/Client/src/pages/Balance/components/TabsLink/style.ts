import styled from 'styled-components';
import { flexCenter } from '../../../../styles/index';

export const TabList = styled.ul`
	${flexCenter}
	border-bottom: 1px solid var(--lightgray);
`;

export const TabItem = styled.li`
	position: relative;
	height: 60px;
	line-height: 60px;
	flex: 1;
	text-align: center;
	a {
		display: block;
		text-decoration: none;
		font-size: 18px;
		color: #333;
	}
	a.active {
		font-weight: 700;
		color: var(--yellow);
	}
	a.active::after {
		position: absolute;
		bottom: -1px;
		content: '';
		display: block;
		width: 100%;
		height: 3px;
		background-color: var(--yellow);
	}
`;
