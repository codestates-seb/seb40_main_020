import styled from 'styled-components';
import { container } from 'styles';

interface Props {
	isLeftMargin: boolean;
}

export const Wapper = styled.main<Props>`
	${container}
	aside {
		margin-left: ${(props) => (props.isLeftMargin ? '30px' : '0')};
	}
`;

export const Container = styled.section`
	flex: 1;
`;

export const Aside = styled.aside`
	width: 400px;
`;
