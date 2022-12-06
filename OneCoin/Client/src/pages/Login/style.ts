import styled from 'styled-components';
import { flexCenter } from '../../styles/index';

export const LoginBox = styled.div`
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
	width: 100%;
	height: 600px;
`;

export const Input = styled.input`
	width: 100%;
	height: 40px;
	padding-left: 15px;
	border: 1px solid var(--borderColor);
	box-sizing: border-box;
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

export const SubmitButton = styled.button`
	width: 100%;
	height: 50px;
	background: white;
	font-size: 16px;
	border: 1px;
	border-width: 1px;
	background: var(--yellow);
	cursor: pointer;
`;

export const Errormsg = styled.p`
	color: #bf1650;
	margin: 10px 0 5px 0;
	font-size: 13px;
`;

export const InputContainer = styled.div`
	margin: 0 0 10px 0;
	display: flex;
	flex-direction: column;
	align-self: center;
	width: 100%;
`;

export const StyledDiv = styled.div`
	text-align: center;
	line-height: normal;
	width: 100%;
	font-size: 24px;
	font-weight: 700;
	margin-bottom: 20px;
`;

export const MentDiv = styled.div`
	display: flex;
	justify-content: center;
`;

export const MentSpan = styled.span`
	margin-top: 15px;
	text-align: center;
	font-size: 0.9rem;
	margin-left: 20px;
	cursor: pointer;
`;

export const KakaoButton = styled.button`
	width: 100%;
	height: 50px;
	margin-top: 15px;
	border: 0 none;
	cursor: pointer;
	background: #fee500;
	${flexCenter}
	svg {
		margin: 3px 3px 6px 3px;
		font-size: 1.2rem;
		color: #000;
	}
`;
