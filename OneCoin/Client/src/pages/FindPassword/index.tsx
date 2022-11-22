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
		<div>
			<FindPasswordBox>
				<Form>
					<StyledDiv>비밀번호 찾기</StyledDiv>
					<InputContainer>
						<Input placeholder="이메일 주소" />
					</InputContainer>
					<SubmitButton>인증메일 보내기</SubmitButton>
				</Form>
			</FindPasswordBox>
		</div>
	);
}

export default FindPassword;
