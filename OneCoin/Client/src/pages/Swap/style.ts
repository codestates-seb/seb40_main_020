import styled from 'styled-components';

export const SwapBox = styled.div`
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
	width: 100%;
	height: calc(100vh - 270px);
`;

export const Form = styled.form`
	display: flex;
	flex-direction: column;
	align-items: center;
	padding: 70px;
	background: white;
	& .exchangeBTN {
		cursor: pointer;
		background: white;
		border: none;
	}
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
	flex-direction: row;
	align-self: center;
	border: 1px solid var(--borderColor);
	& .coin {
		border: none;
	}
`;

export const Input = styled.input`
	width: 435px;
	height: 50px;
	padding-left: 15px;
	border: none;
	box-sizing: border-box;
`;

export const SubmitButton = styled.button`
	width: 500px;
	height: 44px;
	margin: 20px 0px 0px 0px;
	font-size: 16px;
	border: 1px;
	border-width: 1px;
	background: var(--yellow);
	cursor: pointer;
`;
