import React from 'react';
import { ToggleComponent } from './style';
import { BsChatDots } from 'react-icons/bs';

interface Props {
	chatClickHandler: () => void;
}

function Toggle({ chatClickHandler }: Props) {
	return (
		<ToggleComponent onClick={chatClickHandler}>
			<BsChatDots />
		</ToggleComponent>
	);
}

export default Toggle;
