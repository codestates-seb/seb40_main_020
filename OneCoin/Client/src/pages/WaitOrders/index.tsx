import React, { useState } from 'react';
import {
	INVERSTMENTS_LIST,
	WAIT_ORDERS_TBODY,
} from 'utills/constants/investments';
import TabsLink from '../Balance/components/TabsLink/index';
import Layout from 'components/Layout';
import { WAIT_ORDERS_THEAD } from '../../utills/constants/investments';
import Table from '../../components/Table/index';
import Pagination, {
	OnPageChangeCallback,
} from 'pages/Balance/components/Pagination';

import { Wrapper } from './style';

const WaitOrders = () => {
	const [jumpToPage, setJumpToPage] = useState(0);
	const onPageChanged: OnPageChangeCallback = (selectedItem) => {
		const newPage = selectedItem.selected;
		setJumpToPage(newPage);
	};

	return (
		<Layout>
			<Wrapper>
				<TabsLink array={INVERSTMENTS_LIST} />
				<Table
					headerGroups={WAIT_ORDERS_THEAD}
					bodyDatas={WAIT_ORDERS_TBODY.data}
				/>
				<Pagination
					currentPage={jumpToPage}
					pageCount={WAIT_ORDERS_TBODY.pageInfo.totalPages}
					onPageChange={onPageChanged}
				/>
			</Wrapper>
		</Layout>
	);
};

export default WaitOrders;
