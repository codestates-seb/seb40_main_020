import React, { useRef, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { useForm } from 'react-hook-form';
import {
	SignUpBox,
	DoubleCheckBox,
	Errormsg,
	Input,
	InputContainer,
	Form,
	SecondInput,
	SubmitButton,
	StyledDiv,
	SubDiv,
} from './style';
import Layout from 'components/Layout';

type Inputs = {
	displayName: string;
	email: string;
	password: string;
	passwordConfirm: string;
};

function SignUp() {
	const [error, setError] = useState('');
	const navigate = useNavigate();
	const {
		register,
		handleSubmit,
		watch,

		formState: { errors },
	} = useForm<Inputs>({ mode: 'onChange' });

	const onLogin = async (data: Inputs) => {
		// 회원가입 api 자리
		const userData = {
			email: data.email,
			displayName: data.displayName,
			password: data.password,
		};

		return axios
			.post(`${process.env.REACT_APP_SERVER_URL}/api/users`, { ...userData })
			.then(() => {
				navigate('/login');
				alert('이메일이 전송되었습니다. 이메일을 확인해주세요.');
			})
			.catch((err) => {
				setError(err.response.data.message);
				setTimeout(() => {
					setError('');
				}, 1000);
			});
	};
	// 닉네임, 이메일 중복확인
	const displayName = useRef<string | null>(null);
	const onDisplayCheck = async () => {
		displayName.current = watch('displayName');
		try {
			const res = await axios.get(
				`${process.env.REACT_APP_SERVER_URL}/api/users/duplicate-display-name?displayName=${displayName.current}`
			);
			return res;
		} catch (err) {
			alert('닉네임이 중복되었습니다.');
		}
	};

	const email = useRef<string | null>(null);
	const onEmailCheck = async () => {
		email.current = watch('email');
		try {
			const res = await axios.get(
				`${process.env.REACT_APP_SERVER_URL}/api/users/duplicate-email?email=${email.current}`
			);
			return res;
		} catch (err) {
			alert('이메일이 중복되었습니다.');
		}
	};

	const password = useRef<string | null>(null);
	password.current = watch('password');

	return (
		<div>
			<Layout isLeftSidebar={false}>
				<SignUpBox>
					<Form onSubmit={handleSubmit(onLogin)}>
						<StyledDiv>회원가입</StyledDiv>
						<SubDiv>로그인 ID(이메일) 및 비밀번호를 설정해주세요.</SubDiv>
						<InputContainer>
							<div>
								<SecondInput
									placeholder="닉네임을 입력해주세요"
									type={'text'}
									id="displayName"
									{...register('displayName', {
										required: true,
										minLength: 2,
										maxLength: 16,
										pattern: /^[ㄱ-ㅎ|가-힣|a-z|A-Z]+$/,
										//ㄱ-ㅎ|가-힣|a-z|A-Z 한글,영어
										//a-zA-Z 영어로만
									})}
								/>
								<DoubleCheckBox onClick={onDisplayCheck}>
									중복확인
								</DoubleCheckBox>
							</div>
							{errors.displayName && errors.displayName.type === 'required' && (
								<Errormsg>⚠ 닉네임을 입력해주세요.</Errormsg>
							)}
							{errors.displayName && errors.displayName.type === 'pattern' && (
								<Errormsg>⚠ 닉네임은 한글,영어만 가능합니다.</Errormsg>
							)}
							{errors.displayName &&
								errors.displayName.type === 'minLength' && (
									<Errormsg>⚠ 최소 길이는 2자 이상여야 합니다</Errormsg>
								)}
							{errors.displayName &&
								errors.displayName.type === 'maxLength' && (
									<Errormsg>⚠ 최대 길이는 16자 이하여야 합니다</Errormsg>
								)}
						</InputContainer>
						<InputContainer>
							<div>
								<SecondInput
									placeholder="이메일을 입력해주세요"
									type={'email'}
									id="Email"
									{...register('email', {
										required: true,
										pattern: /^\S+@\S+$/i,
										maxLength: 50,
									})}
								/>
								<DoubleCheckBox onClick={onEmailCheck}>중복확인</DoubleCheckBox>
							</div>
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
								placeholder="8~16자리로 입력해주세요"
								type={'password'}
								id="password"
								{...register('password', {
									required: true,
									minLength: 8,
									pattern:
										/^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,25}$/,
								})}
							/>
							<p>영문 대소문자/숫자/특수문자 총 3가지 이상 조합</p>
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
						<InputContainer>
							<Input
								placeholder="비밀번호를 다시 입력해주세요"
								type={'password'}
								id="passwordConfirm"
								{...register('passwordConfirm', {
									required: true,
									validate: (value) => value === password.current,
								})}
							/>
							{errors.passwordConfirm &&
								errors.passwordConfirm.type !== 'passwordConfirm' && (
									<Errormsg>⚠ 비밀번호가 일치하지 않습니다</Errormsg>
								)}
						</InputContainer>
						<SubmitButton type={'submit'}>회원가입</SubmitButton>
					</Form>
				</SignUpBox>
			</Layout>
		</div>
	);
}

export default SignUp;
