import styled from 'styled-components';

export const SignUpBox = styled.div`
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
	width: 100%;
	height: calc(100vh - 50px);
`;

export const DoubleCheckBox = styled.button`
	width: 100px;
	height: 40px;
	margin-left: 8px;
	/* color: var(--yellow); */
	background: white;
	font-size: 16px;
	border: 1px;
	border-width: 1px;
	box-shadow: var(--yellow) 0px 0px 0px 1px;
	background: white;
`;

export const Errormsg = styled.p`
	color: #bf1650;
	margin: 3px;
	font-size: 13px;
`;

export const Input = styled.input`
	width: 420px;
	height: 40px;
	padding-left: 15px;
	border: 1px solid var(--borderColor);
	box-sizing: border-box;
`;

export const Label = styled.label`
	font-size: 15px;
	font-weight: 500;
	margin-bottom: 3px;
`;

export const InputContainer = styled.div`
	margin: 0 0 5px 0;
	display: flex;
	flex-direction: column;
	align-self: center;
	/* margin-bottom: 15px; */
	height: 70px;
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

export const SecondInput = styled.input`
	width: 312px;
	height: 40px;
	padding-left: 15px;
	border: 1px solid var(--borderColor);
	box-sizing: border-box;
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
	justify-content: center;
	font-size: 24px;
	height: 43px;
`;

export const SubDiv = styled.div`
	display: flex;
	justify-content: center;
	font-size: 16px;
	color: #aa9f9f;
	height: 29px;
	margin-top: 4px;
	margin-bottom: 8px;
`;
