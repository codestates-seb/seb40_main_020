import styled from 'styled-components';
import { StyleProps } from './index';

export const ButtonComponent = styled.button<StyleProps>`
	width: ${({ style }) => (style?.width ? `${style.width}px` : '100%')};
	height: ${({ style }) => (style?.height ? `${style.height}px` : '100%')};
	background: ${({ style }) =>
		style?.backgroundColor ? style.backgroundColor : 'var(--yellow)'};
	color: ${({ style }) => (style?.color ? style.color : '#000')};
	border: ${({ style }) =>
		style?.border ? style.border : '1px solid var(--borderColor)'};

	font-size: ${({ style }) =>
		style?.fontSize ? `${style.fontSize}px` : '100%'};
	font-weight: ${({ style }) =>
		style?.fontWeight ? `${style.fontWeight}px` : '100%'};
	cursor: pointer;
	display: flex;
	justify-content: center;
	align-items: center;
`;
