import React, { useState, useEffect } from 'react';
import { INVERSTMENTS_LIST } from 'utills/constants/investments';
import TabsLink from '../Balance/components/TabsLink/index';
import Layout from 'components/Layout';
import { WAIT_ORDERS_THEAD } from '../../utills/constants/investments';

import { Wrapper } from './style';
import { useRecoilValue } from 'recoil';
import { isLogin, coinDataState } from '../../store';
import { dateCalc } from '../../utills/calculate';
import { getNonTrading, deleteOrder } from '../../api/exchange';
import { NonTradingOders } from '../../utills/types';
import Alert from 'components/Alert';
import Button from 'components/Button';
import { codeCoin } from 'utills/coinCode';

const WaitOrders = () => {
	const coinData = useRecoilValue(coinDataState);
	const login = useRecoilValue(isLogin);
	const [data, setData] = useState<NonTradingOders[]>([]);
	const getNonTradingData = async () => {
		try {
			const res = await getNonTrading();
			setData(res);
		} catch (err) {
			console.log(err);
		}
	};
	const deleteOrderData = async (n: number) => {
		try {
			await deleteOrder(n);
			getNonTradingData();
			Alert('주문이 취소되었습니다');
		} catch (err) {
			Alert('주문 취소가 실패했습니다');
		}
	};
	useEffect(() => {
		if (login) getNonTradingData();
	}, []);

	return (
		<Layout>
			<Wrapper>
				<TabsLink array={INVERSTMENTS_LIST} />
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
									<td>{codeCoin(coinData, v.code, 'coin')}</td>
									<td>{v.orderType === 'ASK' ? '매도' : '매수'}</td>
									<td>{(+v.limit).toLocaleString()}</td>
									<td>{+v.amount + +v.completedAmount}</td>
									<td>{v.amount}</td>
									<td onClick={() => deleteOrderData(v.orderId)}>
										<Button style={{ border: 'none', borderRadius: '5px' }}>
											취소
										</Button>
									</td>
								</tr>
							))}
					</tbody>
				</table>
			</Wrapper>
		</Layout>
	);
};

export default WaitOrders;
