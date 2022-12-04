import React, { useEffect, useState } from 'react';
import Layout from 'components/Layout';
import { Wrapper } from './style';
import api from '../../api';
import { useNavigate } from 'react-router-dom';
import { useRecoilState } from 'recoil';
import { isLogin } from '../../store';
import Alert from 'components/Alert';

interface User {
	id: number;
	displayName: string;
	email: string;
	imagePath: string;
}

const MyPage = () => {
	const navigate = useNavigate();
	const [isUseLogin, setIsUseLogin] = useRecoilState(isLogin);
	const [displayName, setDisplayName] = useState('');
	const [password, setPassword] = useState('');
	const [user, setUser] = useState<User>();

	const getUser = async () => {
		try {
			const res = await api.get('/api/users/my-information');
			setUser(res.data.data);
			console.log(res.data.data.imagePath);
		} catch (error) {
			console.log(error);
		}
	};

	const onClickDelete = async () => {
		try {
			const result = confirm('정말로 회원탈퇴 하시겠습니까?');
			if (result) {
				api.delete('/api/users').then(() => {
					sessionStorage.removeItem('login-token');
					sessionStorage.removeItem('login-refresh');
					setIsUseLogin(false);
					Alert('회원탈퇴가 완료되었습니다.');
					navigate('/');
				});
			}
		} catch (err) {
			console.log('안돼요');
		}
	};

	const changingDisplayName = async () => {
		try {
			await api.patch('/api/users/display-name', { displayName }).then(() => {
				confirm('정말로 닉네임을 바꾸시겠습니까?');
				navigate('/');
				Alert('닉네임 수정이 완료되었습니다.');
			});
		} catch (err) {
			console.log(err);
			Alert('닉네임을 확인해주세요');
		}
	};

	const changingPassword = async () => {
		try {
			await api.patch('/api/users/reset-passwords', { password }).then(() => {
				confirm('정말로 비밀번호를 바꾸시겠습니까?');
				navigate('/');
				Alert('비밀번호 수정이 완료되었습니다.');
			});
		} catch (err) {
			console.log(err);
			Alert('비밀번호를 확인해주세요');
		}
	};
	const onChangeDisplayName = (e: React.ChangeEvent<HTMLInputElement>) => {
		setDisplayName(e.target.value);
	};
	const onChangePassword = (e: React.ChangeEvent<HTMLInputElement>) => {
		setPassword(e.target.value);
	};

	useEffect(() => {
		getUser();
	}, []);

	return (
		<Layout isLeftSidebar={false}>
			<Wrapper>
				<ul className="side-menu">
					<li>회원정보 수정</li>
					<li onClick={onClickDelete}>회원탈퇴</li>
				</ul>
				<div className="content-wrapper">
					<div className="profile-box">
						<img src={`${user?.imagePath}`} alt="profile 이미지" />
						<ul>
							<li>닉네임: {user?.displayName as string}</li>
							<li>이메일: {user?.email as string}</li>
						</ul>
					</div>
					<div className="input-box">
						<span>닉네임 변경</span>
						<input
							type="text"
							placeholder="한글,영어 2글자 이상"
							onChange={onChangeDisplayName}
						/>
						<button onClick={changingDisplayName}>닉네임 수정하기</button>
					</div>
					<div className="input-box">
						<span>비밀번호 변경</span>
						<input
							type="password"
							placeholder="8~16자리로 입력해주세요"
							onChange={onChangePassword}
						/>
						<button onClick={changingPassword}>비밀번호 수정하기</button>
						<p>영문 대소문자/숫자/특수문자 총 3가지 이상 조합</p>
					</div>
				</div>
			</Wrapper>
		</Layout>
	);
};

export default MyPage;
