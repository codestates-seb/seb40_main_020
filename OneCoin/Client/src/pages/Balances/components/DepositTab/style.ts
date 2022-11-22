import styled from 'styled-components';
import { flexRowBetween } from 'styles';

export const Wrapper = styled.div`
	.title-menu {
		height: 60px;
	}
`;

export const Info = styled.p`
	${flexRowBetween}
	border-bottom:1px solid var(--borderColor);
	padding: 0 30px;
	font-weight: 700;
	height: 60px;
	span {
		font-weight: 400;
	}
	strong {
		font-weight: 700;
	}
	.icon {
		cursor: pointer;
	}
`;
