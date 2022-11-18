import React, { useState } from 'react';

import Layout from 'components/Layout';
import Table from 'components/Table';
import {
	HISTORY_TBODY,
	HISTORY_THEAD,
	INVERSTMENTS_LIST,
} from 'utills/constants/investments';
import Pagination, {
	OnPageChangeCallback,
} from 'pages/Balance/components/Pagination';
import TabsLink from 'pages/Balance/components/TabsLink';
import HistoryHeader from './components/HistoryHeader';

const History = () => {
	const [jumpToPage, setJumpToPage] = useState(0);
	const onPageChanged: OnPageChangeCallback = (selectedItem) => {
		const newPage = selectedItem.selected;
		setJumpToPage(newPage);
	};

	return (
		<Layout>
			<TabsLink array={INVERSTMENTS_LIST} />
			<HistoryHeader />
			<Table headerGroups={HISTORY_THEAD} bodyDatas={HISTORY_TBODY.data} />
			<Pagination
				currentPage={jumpToPage}
				pageCount={HISTORY_TBODY.pageInfo.totalPages}
				onPageChange={onPageChanged}
			/>
		</Layout>
	);
};

export default History;
