import Layout from 'components/Layout';
import React from 'react';
import {
	FindPasswordBox,
	Form,
	Input,
	InputContainer,
	SubmitButton,
	StyledDiv,
} from '../FindPassword/style';

function ResetPassword() {
	return (
		<Layout isLeftSidebar={false}>
			<FindPasswordBox>
				<Form>
					<StyledDiv>비밀번호 찾기</StyledDiv>
					<InputContainer>
						<Input placeholder="비밀번호" />
					</InputContainer>
					<InputContainer>
						<Input placeholder="비밀번호 재설정" />
					</InputContainer>
					<SubmitButton>비밀번호 재설정</SubmitButton>
				</Form>
			</FindPasswordBox>
		</Layout>
	);
}

export default ResetPassword;
