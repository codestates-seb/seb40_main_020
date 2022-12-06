import Layout from 'components/Layout';
import React from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import {
	LoginBox,
	Form,
	Input,
	Errormsg,
	SubmitButton,
	InputContainer,
	StyledDiv,
	MentDiv,
	MentSpan,
	KakaoButton,
} from './style';
import { userLogin } from 'api/user';
import { RiKakaoTalkFill } from 'react-icons/ri';
import Alert from 'components/Alert';

type EnterForm = {
	email: string;
	password: string;
};

function Login() {
	const navigate = useNavigate();
	const {
		register,
		handleSubmit,
		formState: { errors },
	} = useForm<EnterForm>({
		mode: 'onChange',
	});

	const onLogin = async (data: EnterForm) => {
		userLogin
			.login(data)
			.then((res) => {
				if (res.authorization && res.refresh) {
					localStorage.setItem('login-token', res.authorization);
					localStorage.setItem('login-refresh', res.refresh);
					navigate('/');
				}
			})
			.catch(() => {
				setTimeout(() => {
					Alert('아이디, 비밀번호를 확인해주세요');
				}, 1000);
			});
	};

	const onKakaoLogin = async () => {
		window.location.assign(
			`${process.env.REACT_APP_SERVER_URL}/oauth2/authorization/kakao`
		);
	};

	return (
		<Layout isLeftSidebar={false}>
			<LoginBox>
				<Form onSubmit={handleSubmit(onLogin)}>
					<StyledDiv>로그인</StyledDiv>
					<InputContainer>
						<Input
							placeholder="이메일을 입력해주세요"
							type={'email'}
							id="email"
							{...register('email', {
								required: true,
								pattern: /^\S+@\S+$/i,
								maxLength: 50,
							})}
						/>
						{errors.email && errors.email.type === 'required' && (
							<Errormsg>⚠ 이메일을 입력해주세요.</Errormsg>
						)}
						{errors.email && errors.email.type === 'pattern' && (
							<Errormsg>⚠ 이메일 형식이여야 합니다.</Errormsg>
						)}
						{errors.email && errors.email.type === 'maxLength' && (
							<Errormsg>⚠ 최대 길이는 50자 이하여야 합니다</Errormsg>
						)}
					</InputContainer>
					<InputContainer>
						<Input
							placeholder="비밀번호를 입력해주세요"
							type={'password'}
							id="password"
							{...register('password', {
								required: true,
								minLength: 8,
								pattern: /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,25}$/,
							})}
						/>
						{errors.password && errors.password.type === 'required' && (
							<Errormsg>⚠ 비밀번호를 입력해주세요</Errormsg>
						)}
						{errors.password && errors.password.type === 'minLength' && (
							<Errormsg>⚠ 최소 길이는 8자 이상이여야 합니다</Errormsg>
						)}
						{errors.password && errors.password.type === 'pattern' && (
							<Errormsg>
								⚠ 숫자+영문자+특수문자 8자리 이상이여야 합니다
							</Errormsg>
						)}
					</InputContainer>
					<SubmitButton type="submit">로그인</SubmitButton>
					<InputContainer>
						<KakaoButton type="button" onClick={onKakaoLogin}>
							<RiKakaoTalkFill />
							<span>카카오계정으로 로그인</span>
						</KakaoButton>
					</InputContainer>
					<MentDiv>
						<MentSpan onClick={() => navigate('/signup')}>회원가입</MentSpan>
						<MentSpan onClick={() => navigate('/findpassword')}>
							비밀번호 찾기
						</MentSpan>
					</MentDiv>
				</Form>
			</LoginBox>
		</Layout>
	);
}

export default Login;
