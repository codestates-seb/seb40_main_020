import React, { useState } from 'react';
import Table from 'components/Table';
import {
	DEPOSIT_TBODY,
	DEPOSIT_THEAD,
} from '../../../../utills/constants/balances';
import Pagination, {
	OnPageChangeCallback,
} from 'pages/Balance/components/Pagination';

import { Wrapper } from './style';

const DepositDetailTab = () => {
	const [jumpToPage, setJumpToPage] = useState(0);
	const onPageChanged: OnPageChangeCallback = (selectedItem) => {
		const newPage = selectedItem.selected;
		setJumpToPage(newPage);
	};

	return (
		<Wrapper>
			<Table headerGroups={DEPOSIT_THEAD} bodyDatas={DEPOSIT_TBODY.data} />
			<Pagination
				currentPage={jumpToPage}
				pageCount={DEPOSIT_TBODY.pageInfo.totalPages}
				onPageChange={onPageChanged}
			/>
		</Wrapper>
	);
};

export default DepositDetailTab;
