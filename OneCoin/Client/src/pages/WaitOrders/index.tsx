import React, { useState, useEffect } from 'react';
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
import { useRecoilValue } from 'recoil';
import { isLogin } from '../../store';
import { dateCalc } from '../../utills/calculate';
import { getNonTrading } from '../../api/exchange';
import { NonTradingOders } from '../../utills/types';

const WaitOrders = () => {
	const login = useRecoilValue(isLogin);
	const [jumpToPage, setJumpToPage] = useState(0);
	const onPageChanged: OnPageChangeCallback = (selectedItem) => {
		const newPage = selectedItem.selected;
		setJumpToPage(newPage);
	};
	const [data, setData] = useState<NonTradingOders[]>([]);
	const getNonTradingData = async () => {
		try {
			const res = await getNonTrading();
			setData(res);
		} catch (err) {
			console.log(err);
		}
	};
	useEffect(() => {
		if (login) getNonTradingData();
	}, []);
	return (
		<Layout>
			<Wrapper>
				<TabsLink array={INVERSTMENTS_LIST} />
				{/* <Table headerGroups={WAIT_ORDERS_THEAD} bodyDatas={data} /> */}
				<table>
					<thead>
						<tr>
							{WAIT_ORDERS_THEAD.map((v, i) => (
								<th key={i}>{v}</th>
							))}
						</tr>
					</thead>
					<tbody>
						{data &&
							data.map((v, i) => (
								<tr key={i}>
									<td>{dateCalc(v.orderTime)}</td>
									<td>-</td>
									<td>{v.orderType === 'ASK' ? '매도' : '매수'}</td>
									<td>{v.limit}</td>
									<td>{+v.amount + +v.completedAmount}</td>
									<td>{v.amount}</td>
									<td>{'취소'}</td>
								</tr>
							))}
					</tbody>
				</table>
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
