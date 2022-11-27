import React from 'react';
import { CgArrowsExchangeAltV } from 'react-icons/cg';
import {
	SwapBox,
	Form,
	StyledDiv,
	InputContainer,
	Input,
	SubmitButton,
} from './style';

function Swap() {
	return (
		<SwapBox>
			<Form>
				<StyledDiv>Swap</StyledDiv>
				<InputContainer>
					<label>From</label>
					<select name="coin" id="coin">
						<option value="USDT">USDT</option>
						<option value="BTC">BTC</option>
						<option value="DOGE">DOGE</option>
					</select>
					<Input />
				</InputContainer>
				<button>
					<CgArrowsExchangeAltV size={30} />
				</button>
				<InputContainer>
					<label>To</label>
					<select name="coin" id="coin">
						<option value="BTC">BTC</option>
						<option value="USDT">USDT</option>
						<option value="DOGE">DOGE</option>
					</select>
					<Input />
				</InputContainer>
				<SubmitButton>스왑확인</SubmitButton>
			</Form>
		</SwapBox>
	);
}

export default Swap;
