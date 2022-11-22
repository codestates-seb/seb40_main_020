import React from 'react';
import { HeaderComponent } from './style';
import { useNavigate } from 'react-router-dom';
import logo from '../../assets/images/one.png';
import { HEADER_LIST } from '../../utills/constants/investments';

function Header() {
	const navigate = useNavigate();

	const navClickHandler = (path: string) => navigate(path);
	const isLogin = false;
	return (
		<HeaderComponent>
			<div className="content">
				<div className="nav-page nav">
					<img src={logo} onClick={() => navigate('/')} />
					{HEADER_LIST.page.map((v, i) => (
						<div key={i} onClick={() => navClickHandler(v.path)}>
							{v.name}
						</div>
					))}
				</div>
				<div className="nav-member nav">
					{isLogin
						? HEADER_LIST.login.map((v, i) => (
								<div key={i} onClick={() => navClickHandler(v.path)}>
									{v.name}
								</div>
						  ))
						: HEADER_LIST.logout.map((v, i) => (
								<div key={i} onClick={() => navClickHandler(v.path)}>
									{v.name}
								</div>
						  ))}
				</div>
			</div>
		</HeaderComponent>
	);
}

export default Header;
