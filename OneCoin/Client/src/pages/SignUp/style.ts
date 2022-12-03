import styled from 'styled-components';

export const SignUpBox = styled.div`
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
	width: 100%;
	height: 600px;
`;

export const DoubleCheckBox = styled.button`
	width: 100px;
	height: 40px;
	background: white;
	font-size: 15px;
	border: 1px;
	border-width: 1px;
	border: 1px solid var(--borderColor);
	border-left: 0 none;
	background: white;
	cursor: pointer;
	vertical-align: bottom;
`;

export const Errormsg = styled.p`
	color: #bf1650;
	margin: 3px;
	font-size: 13px;
`;

export const Input = styled.input`
	width: 100%;
	height: 40px;
	padding-left: 15px;
	border: 1px solid var(--borderColor);
	box-sizing: border-box;
`;

export const Label = styled.label`
	font-size: 16px;
	font-weight: 500;
	margin-bottom: 3px;
`;

export const InputContainer = styled.div`
	width: 100%;
	margin: 0 0 10px 0;
	display: flex;
	flex-direction: column;
	align-self: center;
	p {
		margin: 10px 0 0;
		font-size: 0.8rem;
	}
`;

export const Form = styled.form`
	display: flex;
	flex-direction: column;
	align-items: center;
	width: 350px;
	padding: 50px 30px;
	background: white;
	border: 1px solid var(--borderColor);
`;

export const SecondInput = styled.input`
	width: 250px;
	height: 40px;
	padding-left: 15px;
	border: 1px solid var(--borderColor);
	box-sizing: border-box;
`;

export const SubmitButton = styled.button`
	width: 100%;
	height: 50px;
	background: white;
	font-size: 15px;
	border: 1px;
	border-width: 1px;
	background: var(--yellow);

	cursor: pointer;
`;

export const StyledDiv = styled.div`
	display: flex;
	justify-content: center;
	font-size: 24px;
	font-weight: 700;
	margin-bottom: 20px;
`;

export const SubDiv = styled.div`
	display: flex;
	justify-content: center;
	font-size: 15px;
	color: #aa9f9f;
	margin-top: 4px;
	margin-bottom: 20px;
`;
