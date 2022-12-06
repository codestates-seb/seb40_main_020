import styled from 'styled-components';

interface Props {
	log: boolean;
}

export const ChattingComponent = styled.div<Props>`
	width: 100%;
	height: 100%;
	box-sizing: border-box;
	display: ${({ log }) => (log ? 'none' : '')};
`;
