import styled from 'styled-components';

export const LoginBox = styled.div`
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
	width: 100%;
	height: 600px;
`;

export const Input = styled.input`
	width: 420px;
	height: 40px;
	padding-left: 15px;
	border: 1px solid var(--borderColor);
	box-sizing: border-box;
`;

export const Form = styled.form`
	display: flex;
	flex-direction: column;
	align-items: center;
	width: 500px;
	padding: 30px;
	background: white;
`;

export const SubmitButton = styled.button`
	width: 420px;
	height: 44px;
	background: white;
	font-size: 16px;
	border: 1px;
	border-width: 1px;
	background: var(--yellow);
	margin-top: 10px;
	cursor: pointer;
`;

export const Errormsg = styled.p`
	color: #bf1650;
	margin: 3px;
	font-size: 13px;
`;

export const InputContainer = styled.div`
	margin: 0 0 5px 0;
	display: flex;
	flex-direction: column;
	align-self: center;
	/* margin-bottom: 15px; */
	height: 60px;
`;

export const StyledDiv = styled.div`
	display: flex;
	justify-content: start;
	width: 400px;
	font-size: 24px;
	height: 43px;
	margin-bottom: 20px;
`;

export const MentDiv = styled.div`
	display: flex;
	justify-content: center;
`;

export const MentSpan = styled.span`
	margin-top: 50px;
	/* margin-right: 40px; */
	width: 90px;
	cursor: pointer;
`;

export const KakaoButton = styled.button`
	margin-top: 10px;
	background: white;
	border: 1px;
	border-width: 1px;
	padding: 5px 160px;
	display: flex;

	cursor: pointer;
`;
