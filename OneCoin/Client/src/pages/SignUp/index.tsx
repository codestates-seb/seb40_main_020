import React, { useRef, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { useForm, SubmitHandler } from 'react-hook-form';
import {
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

	const onSubmit: SubmitHandler<Inputs> = async (data) => {
		// 회원가입 api 자리
		const userData = {
			memberEmail: data.email,
			memberName: data.displayName,
			memberPwd: data.password,
		};
		return axios
			.post('/api/users', { ...userData })
			.then((res) => {
				navigate('/login');
			})
			.catch((err) => {
				setError(err.response.data.message);
				setTimeout(() => {
					setError('');
				}, 2000);
			});
	};

	// const onLogin = async (data: any) => {
	// 	// 회원가입 api 자리
	// 	const userData = {
	// 		memberEmail: data.email,
	// 		memberName: data.displayName,
	// 		memberPwd: data.password,
	// 	};
	// 	return axios
	// 		.post('http://13.209.194.104:8080/users', { ...userData })
	// 		.then((res) => {
	// 			navigate('/login');
	// 		})
	// 		.catch((err) => {
	// 			setError(err.response.data.message);
	// 			setTimeout(() => {
	// 				setError('');
	// 			}, 2000);
	// 		});
	// };

	const password = useRef<string | null>(null);
	password.current = watch('password');

	return (
		<div>
			<Layout isLeftSidebar={false}>
				<Form onSubmit={handleSubmit(onSubmit)}>
					<StyledDiv>회원가입</StyledDiv>
					<SubDiv>로그인 ID(이메일) 및 비밀번호를 설정해주세요.</SubDiv>
					<InputContainer>
						<Input
							placeholder="닉네임을 입력해주세요"
							type={'text'}
							id="displayName"
							{...register('displayName', {
								required: true,
								minLength: 2,
								maxLength: 16,
								pattern: /^[a-zA-Z]+$/,
								//ㄱ-ㅎ|가-힣|a-z|A-Z 한글,영어
							})}
						/>
						{errors.displayName && errors.displayName.type === 'required' && (
							<Errormsg>⚠ 닉네임을 입력해주세요.</Errormsg>
						)}
						{errors.displayName && errors.displayName.type === 'pattern' && (
							<Errormsg>⚠ 닉네임은 영어로만 가능합니다.</Errormsg>
						)}
						{errors.displayName && errors.displayName.type === 'minLength' && (
							<Errormsg>⚠ 최소 길이는 2자 이상여야 합니다</Errormsg>
						)}
						{errors.displayName && errors.displayName.type === 'maxLength' && (
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
							<DoubleCheckBox>중복확인</DoubleCheckBox>
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
							placeholder="영문 대소문자/숫자/특수문자 총 3가지 이상 조합 8~16자리로 입력해주세요"
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
			</Layout>
		</div>
	);
}

export default SignUp;
