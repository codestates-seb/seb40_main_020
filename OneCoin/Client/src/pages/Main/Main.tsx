import React from 'react';
import serviceImg from '../../assets/images/mainPage2.png';
import PriceContainer from './PriceRtc';
import {
	MainTopContainer,
	ContentCotainer,
	ServiceTitle,
	SignupBtn,
} from './style';
const Main = () => {
	return (
		<MainTopContainer>
			<ContentCotainer>
				<ServiceTitle>
					대한민국 최초
					<br />
					탈중앙화 거래소, <em>1Coin</em>
				</ServiceTitle>
				<div className="btnContainer">
					<SignupBtn>1Coin 가입하기</SignupBtn>
					<img src={serviceImg} />
				</div>
			</ContentCotainer>
			<PriceContainer></PriceContainer>
		</MainTopContainer>
	);
};
export default Main;
