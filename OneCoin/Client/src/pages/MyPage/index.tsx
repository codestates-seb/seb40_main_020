import React, { useState } from 'react';
import Layout from 'components/Layout';
import { Wrapper } from './style';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { useRecoilState } from 'recoil';
import { isLogin } from '../../store';
import api from '../../api';

const MyPage = () => {
	const navigate = useNavigate();
	const [isUseLogin, setIsUseLogin] = useRecoilState(isLogin);
	const [displayName, setDisplayName] = useState('');
	const [password, setPassword] = useState('');
	const onClickDelete = async () => {
		try {
			await api.delete('/api/users/123').then((res) => console.log(res));
			sessionStorage.removeItem('login-token');
			sessionStorage.removeItem('login-refresh');
			setIsUseLogin(false);
			navigate('/');
		} catch (err) {
			console.log('안돼요');
		}
	};
	const changingInformation = async () => {
		try {
			await axios.patch(`${process.env.REACT_APP_SERVER_URL}/api/users/1`);
		} catch (err) {
			console.log(err);
		}
	};
	return (
		<Layout isLeftSidebar={false}>
			<Wrapper>
				<ul className="side-menu">
					<li>닉네임 변경</li>
					<li>비밀번호 변경</li>
					<li onClick={onClickDelete}>회원탈퇴</li>
				</ul>
				<div className="content-wrapper">
					<div className="profile-box">
						<img src="" alt="profile 이미지" />
						<ul>
							<li>닉네임: ???</li>
							<li>이메일: 1234@gmail.com</li>
						</ul>
					</div>
					<div className="input-box">
						<span>닉네임 변경</span>
						<input type="text" placeholder="닉네임" />
					</div>
					<div className="input-box">
						<span>비밀번호 변경</span>
						<input type="password" placeholder="비밀번호" />
					</div>
					<button onClick={changingInformation}>수정하기</button>
				</div>
			</Wrapper>
		</Layout>
	);
};

export default MyPage;
