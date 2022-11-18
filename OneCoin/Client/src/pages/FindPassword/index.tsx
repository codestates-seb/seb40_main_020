import Layout from 'components/Layout';
import React from 'react';
import {
	FindPasswordBox,
	Form,
	Input,
	InputContainer,
	SubmitButton,
	StyledDiv,
} from './style';

function FindPassword() {
	return (
		<Layout isLeftSidebar={false}>
			<FindPasswordBox>
				<Form>
					<StyledDiv>비밀번호 찾기</StyledDiv>
					<InputContainer>
						<Input placeholder="이메일 주소" />
					</InputContainer>
					<SubmitButton>인증메일 보내기</SubmitButton>
				</Form>
			</FindPasswordBox>
		</Layout>
	);
}

export default FindPassword;
