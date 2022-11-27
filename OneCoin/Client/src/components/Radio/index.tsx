import React, { useRef } from 'react';
import { RadioComponent } from './style';

interface Props {
	onClick: (value: string) => void;
	value: string;
	name: string;
	defaultChecked?: boolean;
	children: string;
	complete: string;
}

function Radio({
	onClick,
	value,
	name,
	defaultChecked = false,
	children,
	complete,
}: Props) {
	return (
		<RadioComponent onClick={() => onClick(value)}>
			<input
				type="radio"
				value={value}
				name={name}
				checked={complete === value}
				readOnly
			/>
			<label htmlFor={value}>{children}</label>
		</RadioComponent>
	);
}

export default Radio;
