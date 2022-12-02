import React, { useEffect } from 'react';
import { HeaderComponent } from './style';
import { useNavigate } from 'react-router-dom';
import logo from '../../assets/images/one.png';
import { HEADER_LIST } from '../../utills/constants/header';
import { useRecoilState } from 'recoil';
import { isLogin } from '../../store';

function Header() {
	const navigate = useNavigate();
	const [isUseLogin, setIsUseLogin] = useRecoilState(isLogin);

	const navClickHandler = (path: string) => navigate(path);

	const LoginHanddler = () => {
		const token = sessionStorage.getItem('login-token');
		if (token === null) {
			setIsUseLogin(false);
		} else {
			setIsUseLogin(true);
		}
	};

	useEffect(() => {
		LoginHanddler();
	}, []);

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
					{isUseLogin
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
					{isUseLogin && (
						<div
							className="nav-member nav"
							onClick={() => {
								sessionStorage.removeItem('login-token');
								sessionStorage.removeItem('login-refresh');
								navigate('/');
								setIsUseLogin(false);
							}}
						>
							로그아웃
						</div>
					)}
				</div>
			</div>
		</HeaderComponent>
	);
}

export default Header;
