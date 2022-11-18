import React from 'react';
import { HeaderComponent } from './style';
import logo from '../../assets/images/open.png';

function Header() {
	return (
		<HeaderComponent>
			<img src={logo} />
		</HeaderComponent>
	);
}

export default Header;
