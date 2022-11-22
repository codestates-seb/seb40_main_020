import React from 'react';

import { Wrapper } from './style';

const ChargingTab = () => {
	return (
		<Wrapper>
			<div className="inner-wrap">
				<span>입금금액(KRW)</span>
				<div>
					<input type="text" placeholder="최소 5,000 KRW" />
				</div>
			</div>
			<div className="content-wrap">
				<p>* 입금 실행 시, 신청한 금액만큼 포인트 충전이 됩니다.</p>
			</div>
			<button>입금신청</button>
		</Wrapper>
	);
};

export default ChargingTab;
