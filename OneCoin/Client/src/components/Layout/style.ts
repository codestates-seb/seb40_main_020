import styled from 'styled-components';
import { container } from 'styles';
import { flexCenter } from '../../styles/index';

interface Props {
	isLeftMargin: boolean;
}

export const Wapper = styled.main<Props>`
	${container}
	min-height: calc(100vh - 270px);
	aside {
		margin-left: ${(props) => (props.isLeftMargin ? '30px' : '0')};
	}
`;

export const Container = styled.section`
	flex: 1;
	width: 100%;
`;

export const Aside = styled.aside`
	width: 400px;
`;
