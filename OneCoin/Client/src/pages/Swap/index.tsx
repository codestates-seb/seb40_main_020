import React from 'react';
import Button from 'components/Button';
import Layout from 'components/Layout';
import { useNavigate } from 'react-router-dom';
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
	const navigate = useNavigate();
	return (
		<Layout>
			<Form>
				<StyledDiv>Swap</StyledDiv>
				<InputContainer>
					<select name="coin" className="coin">
						<option value="KRW">KRW</option>
						<option value="BTC">BTC</option>
						<option value="DOGE">DOGE</option>
					</select>
					<Input />
				</InputContainer>
				<button className="exchangeBTN">
					<CgArrowsExchangeAltV size={30} />
				</button>
				<InputContainer>
					<select name="coin" className="coin">
						<option value="BTC">BTC</option>
						<option value="USDT">USDT</option>
						<option value="DOGE">DOGE</option>
					</select>
					<Input />
				</InputContainer>
				<SubmitButton onClick={() => navigate('/swapresult')}>
					스왑확인
				</SubmitButton>
			</Form>
		</Layout>
	);
}

export default Swap;
