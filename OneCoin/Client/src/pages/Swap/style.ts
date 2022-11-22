import styled from 'styled-components';

export const SwapBox = styled.div`
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
	width: 100%;
	height: calc(100vh - 50px);
`;

export const Form = styled.form`
	display: flex;
	flex-direction: column;
	align-items: center;
	padding: 70px;
	background: white;
`;

export const StyledDiv = styled.div`
	display: flex;
	justify-content: start;
	width: 500px;
	font-size: 24px;
	height: 43px;
	margin-bottom: 10px;
`;

export const InputContainer = styled.div`
	margin: 20px 0px 20px 0px;
	display: flex;
	flex-direction: column;
	align-self: center;
`;

export const Input = styled.input`
	width: 500px;
	height: 50px;
	padding-left: 15px;
	border: 1px solid var(—borderColor);
	box-sizing: border-box;
`;

export const SubmitButton = styled.button`
	width: 320px;
	height: 44px;
	margin: 20px 0px 0px 8px;
	background: white;
	font-size: 16px;
	border: 1px;
	border-width: 1px;
	background: var(—yellow);
`;
