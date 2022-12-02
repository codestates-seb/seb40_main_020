import React, { useState, useEffect } from 'react';

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
import { getCompleteTradePage } from '../../api/exchange';
import { dateCalc } from '../../utills/calculate';
import { Wrapper } from './style';
interface Data {
	amount: string;
	code: string;
	commission: string;
	completedTime: string;
	orderTime: string;
	orderType: string;
	price: string;
	settledAmount: string;
	totalAmount: string;
}
interface T {
	data: Data[];
	pageInfo: {
		page: number;
		size: number;
		totalElements: number;
		totalPages: number;
	};
}

const History = () => {
	const [jumpToPage, setJumpToPage] = useState(0);
	const onPageChanged: OnPageChangeCallback = (selectedItem) => {
		const newPage = selectedItem.selected;
		setJumpToPage(newPage);
	};
	const [data, setData] = useState<T>();
	const [period, setPeriod] = useState('w');
	const [type, setType] = useState('ALL');

	const periodHandler = (s: string) => {
		if (s === '1') setPeriod('m');
		else if (s === '3') setPeriod('3m');
		else if (s === '6') setPeriod('6m');
		else setPeriod('w');
	};
	const typeHandler = (s: string) => {
		if (s === 'buy') setType('BID');
		else if (s === 'sell') setType('ASK');
		else if (s === 'deposit') setType('DEPOSIT');
		else if (s === 'swap') setType('SWAP');
		else setType('ALL');
	};

	const getCompleteTradeData = async (code?: string) => {
		try {
			const res = await getCompleteTradePage(
				jumpToPage + 1,
				period,
				type,
				code
			);
			setData(res);
		} catch (err) {
			console.log(err);
		}
	};
	useEffect(() => {
		getCompleteTradeData();
	}, [jumpToPage, period, type]);
	return (
		<Layout>
			<Wrapper>
				<TabsLink array={INVERSTMENTS_LIST} />
				<HistoryHeader
					periodHandler={periodHandler}
					typeHandler={typeHandler}
					getCompleteTradeData={getCompleteTradeData}
				/>
				{/* <Table headerGroups={HISTORY_THEAD} bodyDatas={HISTORY_TBODY.data} /> */}
				<table>
					<thead>
						<tr>
							{HISTORY_THEAD.map((v, i) => (
								<th key={i}>{v}</th>
							))}
						</tr>
					</thead>
					<tbody>
						{data &&
							data.data.map((v, i) => (
								<tr key={i}>
									<td>{dateCalc(v.completedTime)}</td>
									<td>{v.code}</td>
									<td>{v.orderType === 'ASK' ? '매도' : '매수'}</td>
									<td>{(+v.amount).toFixed(8)}</td>
									<td>{(+v.price).toLocaleString()}</td>
									<td>{Math.round(+v.totalAmount).toLocaleString()}</td>
									<td>{Math.round(+v.commission).toLocaleString()}</td>
									<td>
										{Math.round(
											+v.totalAmount + +v.commission
										).toLocaleString()}
									</td>
									<td>{dateCalc(v.orderTime)}</td>
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
		</Layout>
	);
};

export default History;
