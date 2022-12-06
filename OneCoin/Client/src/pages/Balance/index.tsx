import React, { useEffect } from 'react';
import { BALANCE_THEAD, INVERSTMENTS_LIST } from 'utills/constants/investments';
import Layout from '../../components/Layout';
import TableInfo from './components/TableInfo';
import TabsLink from './components/TabsLink';
import { useRecoilState, useRecoilValue } from 'recoil';
import { myCoinsState, coinDataState, isLogin } from '../../store';
import { Wrapper } from './style';
import { getWallet } from '../../api/exchange';
import { MyCoins } from '../../utills/types';
import { proceedsCalculator, rateCalculator } from '../../utills/calculate';

function Balance() {
	const login = useRecoilValue(isLogin);
	const [myCoins, setMyCoins] = useRecoilState(myCoinsState);
	const coinData = useRecoilValue(coinDataState);
	const getWalletData = async () => {
		try {
			const res = await getWallet();
			setMyCoins(res);
		} catch (err) {
			console.log(err);
		}
	};
	const realTimeCalculator = (arr: MyCoins[]) => {
		return [...arr].map((v) => {
			const realTimePrice = coinData.filter((coin) => coin.code === v.code)[0]
				?.ticker?.trade_price as string;
			const avgPrice = Number(v.averagePrice as string);
			const amount = Number(v.amount as string);
			const proceeds = proceedsCalculator(+realTimePrice, avgPrice, amount);
			const rate = rateCalculator(+realTimePrice, avgPrice);
			const priceEvaluation = Math.round(+realTimePrice * amount);
			const change = rate > 0 ? 'rise' : rate < 0 ? 'fall' : '';
			const obj = {
				...v,
				coin: coinData.filter((c) => c.code === v.code)[0].coin,
				totalPrice: Math.round(avgPrice * amount).toLocaleString(),
				priceEvaluation: priceEvaluation
					? priceEvaluation.toLocaleString()
					: '0',
				changePrice: proceeds ? proceeds.toLocaleString() : '0',
				changeRate: rate + '%',
				change,
			};
			return obj;
		});
	};
	useEffect(() => {
		if (login) getWalletData();
	}, []);
	useEffect(() => {
		if (myCoins !== undefined && myCoins.length) {
			const newMyCoins = realTimeCalculator(myCoins);
			setMyCoins(newMyCoins);
		}
	}, [coinData]);
	return (
		<Layout>
			<Wrapper>
				<TabsLink array={INVERSTMENTS_LIST} />
				<TableInfo />
				<div className="title">보유자산 목록</div>
				<table>
					<thead>
						<tr>
							{BALANCE_THEAD.map((v, i) => (
								<th key={i}>{v}</th>
							))}
						</tr>
					</thead>
					<tbody>
						{myCoins &&
							myCoins.map((v, i) => (
								<tr key={i}>
									<td>{v.coin}</td>
									<td>{(+v.amount).toFixed(8)}</td>
									<td>{Math.round(+v.averagePrice).toLocaleString()}</td>
									<td>{v.priceEvaluation}</td>
									<td>{v.totalPrice}</td>
									<td className={v.change}>{v.changePrice}</td>
									<td className={v.change}>{v.changeRate}</td>
								</tr>
							))}
					</tbody>
				</table>
			</Wrapper>
		</Layout>
	);
}

export default Balance;
