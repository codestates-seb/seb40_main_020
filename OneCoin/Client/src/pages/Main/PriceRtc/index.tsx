import React from 'react';
import { PriceRtc, CryptoContainer } from './style';
const PriceContainer = () => {
	return (
		//map 으로 수정
		<PriceRtc>
			<div className="flexContain">
				<CryptoPriceContainer />
				<CryptoPriceContainer />
			</div>
			<div className="flexContain">
				<CryptoPriceContainer />
				<CryptoPriceContainer />
			</div>
			<div className="flexContain">
				<CryptoPriceContainer />
				<CryptoPriceContainer />
			</div>
		</PriceRtc>
	);
};
const CryptoPriceContainer = () => {
	return (
		<CryptoContainer>
			<em>주요 가상자산</em>
			<div>
				<div className="inLineContainer">
					<h2>비트코인</h2>
					<h2 className="color"> 22,679,000</h2>
				</div>
				<div className="inLineContainer">
					<h3>BTC</h3>
					<h3 className="color">-1.95%</h3>
				</div>
			</div>
		</CryptoContainer>
	);
};
export default PriceContainer;
