import styled from 'styled-components';

export const SwapResultBox = styled.div`
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
	height: 336px;
	background: white;
`;

export const HeaderDiv = styled.div`
	width: 130px;
	height: 100px;
	font-size: 28px;
	display: flex;
	justify-content: center;
	align-items: center;
`;

export const StyleDiv = styled.div`
	display: flex;
	align-items: center;
	font-size: 24px;
	margin: 0 40px;
`;

export const StyleButton = styled.button`
	width: 160px;
	height: 44px;
	margin: 50px 0px 0px 12px;
	font-size: 16px;
	border: 1px;
	border-width: 1px;
	cursor: pointer;

	&.gray {
		background: var(-—borderColor);
	}
	&.yellow {
		background: var(--yellow);
	}
`;
