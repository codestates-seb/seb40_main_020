import React from 'react';
import Tab from 'components/Tab';
import { GrRefresh } from 'react-icons/gr';

import { Wrapper, Info } from './style';
import ChargingTab from '../ChargingTab';
import DepositDetailTab from '../DepositDetailTab/index';

const DepositTab = () => {
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
					<strong>0</strong> KRW
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
