import React from 'react';
import { INVERSTMENTS_LIST } from 'utills/constants';
import Layout from './components/Layout';
import TableInfo from './components/TableInfo';
import TabsLink from './components/TabsLink';

import { Wrapper } from './style';

function Balance() {
	return (
		<Layout>
			<Wrapper>
				<TabsLink array={INVERSTMENTS_LIST} />
				<TableInfo />
			</Wrapper>
		</Layout>
	);
}

export default Balance;
