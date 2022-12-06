import React, { useState, useEffect } from 'react';
import Table from 'components/Table';
import {
	DEPOSIT_TBODY,
	DEPOSIT_THEAD,
} from '../../../../utills/constants/balances';
import Pagination, {
	OnPageChangeCallback,
} from 'pages/Balance/components/Pagination';

import { getDeposits } from '../../../../api/balance';
import { Wrapper } from './style';

import { useRecoilValue } from 'recoil';
import { isLogin } from '../../../../store';

type Data = {
	createdAt: string;
	depositAmount: number;
	remainingBalance: number;
};
type Info = {
	page: number;
	size: number;
	totalElements: number;
	totalPages: number;
};
interface T {
	data: Data[];
	pageInfo: Info;
}

const DepositDetailTab = () => {
	const login = useRecoilValue(isLogin);
	const [jumpToPage, setJumpToPage] = useState(0);
	const [data, setData] = useState<T>();
	const onPageChanged: OnPageChangeCallback = (selectedItem) => {
		const newPage = selectedItem.selected;
		setJumpToPage(newPage);
	};
	const getDepositsHandler = async () => {
		try {
			const res = await getDeposits(jumpToPage + 1);
			setData(res);
			console.log(res);
		} catch (err) {
			console.log(err);
		}
	};
	useEffect(() => {
		if (login) getDepositsHandler();
	}, []);
	return (
		<Wrapper>
			{/* <Table headerGroups={DEPOSIT_THEAD} bodyDatas={DEPOSIT_TBODY.data} /> */}
			<table>
				<thead>
					<tr>
						{DEPOSIT_THEAD.map((v, i) => (
							<th key={i}>{v}</th>
						))}
					</tr>
				</thead>
				<tbody>
					{data &&
						data.data.map((v, i) => (
							<tr key={i}>
								<td>KRW</td>
								<td>{v.depositAmount.toLocaleString()}</td>
							</tr>
						))}
				</tbody>
			</table>
			<Pagination
				currentPage={jumpToPage}
				pageCount={data?.pageInfo?.totalPages as number}
				onPageChange={onPageChanged}
			/>
		</Wrapper>
	);
};

export default DepositDetailTab;
