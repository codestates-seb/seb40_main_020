import React, { useState } from 'react';
import {
	BALANCE_THEAD,
	INVERSTMENTS_LIST,
	BALANCE_TBODY,
} from 'utills/constants/investments';
import Layout from '../../components/Layout';
import Table from '../../components/Table';
import Pagination, { OnPageChangeCallback } from './components/Pagination';
import TableInfo from './components/TableInfo';
import TabsLink from './components/TabsLink';

import { Wrapper } from './style';

function Balance() {
	const [jumpToPage, setJumpToPage] = useState(0);
	const onPageChanged: OnPageChangeCallback = (selectedItem) => {
		const newPage = selectedItem.selected;
		setJumpToPage(newPage);
	};

	return (
		<Layout>
			<Wrapper>
				<TabsLink array={INVERSTMENTS_LIST} />
				<TableInfo />
				<Table
					title="보유자산 목록"
					headerGroups={BALANCE_THEAD}
					bodyDatas={BALANCE_TBODY.data}
				/>
				<Pagination
					currentPage={jumpToPage}
					pageCount={BALANCE_TBODY.pageInfo.totalPages}
					onPageChange={onPageChanged}
				/>
			</Wrapper>
		</Layout>
	);
}

export default Balance;
