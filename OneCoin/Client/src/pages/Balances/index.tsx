import React from 'react';

import Layout from '../../components/Layout';
import TotalAsset from './components/TotalAsset';
import DepositTab from './components/DepositTab';

import { Wrapper } from './style';

function Balances() {
	return (
		<Layout>
			<Wrapper>
				<TotalAsset />
				<DepositTab />
			</Wrapper>
		</Layout>
	);
}

export default Balances;
