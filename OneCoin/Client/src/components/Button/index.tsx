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
	onClick?: () => void;
}

interface Props extends StyleProps {
	children: string;
}

function Button({ children, style, onClick }: Props) {
	return (
		<ButtonComponent style={style} onClick={onClick}>
			{children}
		</ButtonComponent>
	);
}

export default Button;
