import React, { useEffect } from 'react';
import Tab from 'components/Tab';
import { GrRefresh } from 'react-icons/gr';

import { Wrapper, Info } from './style';
import ChargingTab from '../ChargingTab';
import DepositDetailTab from '../DepositDetailTab/index';
import { getBalance } from '../../../../api/balance';
import { useRecoilState, useRecoilValue } from 'recoil';
import { myBalanceState, isLogin } from '../../../../store';

const DepositTab = () => {
	const login = useRecoilState(isLogin);
	const [myBalance, setMyBalance] = useRecoilState(myBalanceState);
	const getBalanceHandler = async () => {
		try {
			const res = await getBalance();
			setMyBalance(res);
		} catch (err) {
			console.log(err);
		}
	};
	useEffect(() => {
		if (login) getBalanceHandler();
	}, []);
	return (
		<Wrapper>
			<Info>
				KRW 입금
				<span>
					<GrRefresh className="icon" />
				</span>
			</Info>
			<Info>
				보유금액
				<span>
					<strong>{myBalance && myBalance.toLocaleString()}</strong> KRW
				</span>
			</Info>
			<Tab
				menu={['KRW충전', '입금내역']}
				el={[
					<ChargingTab key={'charging-tab'} />,
					<DepositDetailTab key={'deposit-detail-tab'} />,
				]}
			/>
		</Wrapper>
	);
};

export default DepositTab;
