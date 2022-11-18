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
} from './style';

type EnterForm = {
	email: string;
	password: string;
};

function Login() {
	const navigate = useNavigate();
	const {
		register,
		handleSubmit,
		watch,
		formState: { errors },
	} = useForm<EnterForm>({
		mode: 'onChange',
	});
	console.log(watch());
	const onLogin = async () => {
		console.log();
	};
	// 	try {
	// 		const res = await axios.post('/api/members/login', {
	// 			username: data.email,
	// 			password: data.password,
	// 		});
	// 		if (res.status === 200) {
	// 			sessionStorage.setItem('jwt-token', res.headers.authorization);
	// 			sessionStorage.setItem('user', JSON.stringify({ ...res.data.data }));
	// 			dispatch(setUser({ ...res.data.data }));
	// 			navigate('/');
	// 		}
	// 	} catch (err) {
	// 		setError(err.response.data.message);
	// 		setTimeout(() => {
	// 			setError('');
	// 		}, 2000);
	// 	}
	// };

	return (
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
						<Errormsg>⚠ 숫자+영문자+특수문자 8자리 이상이여야 합니다</Errormsg>
					)}
				</InputContainer>
				<SubmitButton type="submit">로그인</SubmitButton>
				{/* {error && <Errormsg>⚠ {error}</Errormsg>} */}
				<MentDiv>
					<MentSpan onClick={() => navigate('/signup')}>회원가입</MentSpan>
					<MentSpan onClick={() => navigate('/')}>비밀번호 찾기</MentSpan>
				</MentDiv>
			</Form>
		</LoginBox>
	);
}

export default Login;
