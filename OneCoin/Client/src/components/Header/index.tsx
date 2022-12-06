import React, { useEffect } from 'react';
import { HeaderComponent } from './style';
import { useNavigate, useLocation } from 'react-router-dom';
import logo from '../../assets/images/one.png';
import { HEADER_LIST } from '../../utills/constants/header';
import { useRecoilState } from 'recoil';
import { isLogin, sessionIdState } from '../../store';
import Alert from 'components/Alert';
import { userIdState } from 'store';
import { exitClient } from 'api/socket';
function Header() {
	const navigate = useNavigate();
	const { pathname } = useLocation();
	const [isUseLogin, setIsUseLogin] = useRecoilState(isLogin);
	const [userId, setUserId] = useRecoilState(userIdState);
	const [sessionId, setSessionId] = useRecoilState(sessionIdState);
	const navClickHandler = (path: string) => navigate(path);

	const LoginHanddler = () => {
		const token = localStorage.getItem('login-token');
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
						<div
							key={i}
							className={
								pathname.split('/')[1] === v.path.split('/')[1] ? 'select' : ''
							}
							onClick={() => {
								navClickHandler(v.path);
							}}
						>
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
								localStorage.removeItem('login-token');
								localStorage.removeItem('login-refresh');
								navigate('/');
								setIsUseLogin(false);
								setUserId(null);
								exitClient();
								setSessionId('');
								Alert('로그아웃 되었습니다');
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
