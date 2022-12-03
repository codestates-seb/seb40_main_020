import styled from 'styled-components';

export const FindPasswordBox = styled.div`
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
	width: 100%;
	height: 600px;
`;

export const Form = styled.form`
	display: flex;
	flex-direction: column;
	align-items: center;
	padding: 50px 30px;
	width: 350px;
	background: #fff;
	border: 1px solid var(--borderColor);
`;

export const Input = styled.input`
	width: 100%;
	height: 40px;
	padding-left: 15px;
	border: 1px solid var(--borderColor);
	box-sizing: border-box;
`;

export const InputContainer = styled.div`
	margin: 0 0 10px 0;
	display: flex;
	flex-direction: column;
	align-self: center;
	width: 100%;
`;

export const SubmitButton = styled.button`
	width: 100%;
	height: 50px;
	font-size: 16px;
	border: 0 none;
	background: var(--yellow);
`;

export const StyledDiv = styled.div`
	text-align: center;
	line-height: normal;
	width: 100%;
	font-size: 24px;
	font-weight: 700;
	margin-bottom: 20px;
`;
