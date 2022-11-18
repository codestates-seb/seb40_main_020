import React from 'react';
import { ButtonComponent } from './style';

export interface StyleProps {
	style?: {
		backgroundColor?: string;
		width?: number;
		height?: number;
		color?: string;
		border?: string;
		fontSize?: number;
		fontWeight?: number;
	};
}

interface Props extends StyleProps {
	children: string;
}

function Button({ children, style }: Props) {
	return <ButtonComponent style={style}>{children}</ButtonComponent>;
}

export default Button;
