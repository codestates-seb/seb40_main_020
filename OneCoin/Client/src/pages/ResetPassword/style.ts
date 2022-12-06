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
	/* height: 100%;
	width: 100%; */
	padding: 60px;
	background: white;
`;

export const Input = styled.input`
	width: 420px;
	height: 40px;
	padding-left: 15px;
	border: 1px solid var(--borderColor);
	box-sizing: border-box;
`;

export const InputContainer = styled.div`
	margin: 0 0 5px 0;
	display: flex;
	flex-direction: column;
	align-self: center;
	/* margin-bottom: 15px; */
	height: 60px;
`;

export const SubmitButton = styled.button`
	width: 320px;
	height: 44px;
	margin-left: 8px;
	background: white;
	font-size: 16px;
	border: 1px;
	border-width: 1px;
	background: var(--yellow);
`;

export const StyledDiv = styled.div`
	display: flex;
	justify-content: start;
	width: 400px;
	font-size: 24px;
	height: 43px;
	margin-bottom: 10px;
`;
