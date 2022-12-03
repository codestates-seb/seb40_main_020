import React, { useState } from 'react';
import { Wrapper } from './style';
import { postDeposit } from '../../../../api/balance';
import { useRecoilState, useRecoilValue } from 'recoil';
import { myBalanceState, isLogin } from '../../../../store';
import Alert from '../../../../components/Alert';

const ChargingTab = () => {
	const login = useRecoilValue(isLogin);
	const [myBalance, setMyBalance] = useRecoilState(myBalanceState);
	const [p, setP] = useState<number>(0);
	const pChangeHandler = (e: React.ChangeEvent<HTMLInputElement>) => {
		if (!Number.isNaN(+e.target.value)) {
			setP(+e.target.value);
		}
	};
	const postDepositHandler = async () => {
		try {
			const data = { depositAmount: p };
			const res = await postDeposit(data);
			setMyBalance(res.remainingBalance);
			Alert('입금 완료.');
		} catch (err) {
			console.log(err);
		}
	};
	const max = 100000000;
	const btnHandler = () => {
		if (login) {
			if (p < 5000) Alert('5,000 KRW이상 입금 가능합니다.');
			else if (p > max)
				Alert(`${max.toLocaleString()} KRW이하 입금 가능합니다.`);
			else {
				postDepositHandler();
			}
		} else {
			Alert('로그인이 필요한 서비스입니다.');
		}
	};
	return (
		<Wrapper>
			<div className="inner-wrap">
				<span>입금금액(KRW)</span>
				<div>
					<input
						type="text"
						placeholder="최소 5,000 KRW"
						value={p}
						onChange={pChangeHandler}
					/>
				</div>
			</div>
			<div className="content-wrap">
				<p>* 입금 실행 시, 신청한 금액만큼 포인트 충전이 됩니다.</p>
			</div>
			<button onClick={btnHandler}>입금신청</button>
		</Wrapper>
	);
};

export default ChargingTab;
