import React, { useState } from 'react';
import Layout from 'components/Layout';
import {
	FindPasswordBox,
	Form,
	Input,
	InputContainer,
	SubmitButton,
	StyledDiv,
} from './style';
import axios from 'axios';

function FindPassword() {
	const [email, setEmail] = useState('');
	const onChange = (e: React.ChangeEvent<HTMLInputElement>) => {
		setEmail(e.target.value);
		console.log(email);
	};
	const onSubmit = async () => {
		try {
			const res = await axios.get(
				`${process.env.REACT_APP_SERVER_URL}/api/users/find-password?email=${email}`
			);
			alert('이메일을 확인해주세요.');
			return res;
		} catch (err) {
			alert('이메일을 확인해주세요.');
		}
	};
	return (
		<Layout isLeftSidebar={false}>
			<FindPasswordBox>
				<Form>
					<StyledDiv>비밀번호 찾기</StyledDiv>
					<InputContainer>
						<Input placeholder="이메일 주소" onChange={onChange} />
					</InputContainer>
					<SubmitButton onClick={onSubmit}>인증메일 보내기</SubmitButton>
				</Form>
			</FindPasswordBox>
		</Layout>
	);
}

export default FindPassword;
