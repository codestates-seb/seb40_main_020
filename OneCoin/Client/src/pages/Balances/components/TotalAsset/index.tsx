import React, { useEffect, useState } from 'react';
import { AiOutlineSearch } from 'react-icons/ai';
import Table from 'components/Table';
import { BALANCES_THEAD, BALANCES_TBODY } from 'utills/constants/balances';
import { Wrapper } from './style';
import { useRecoilValue, useRecoilState } from 'recoil';
import {
	myBalanceState,
	myCoinsState,
	coinDataState,
	isLogin,
} from '../../../../store';
import { getWallet } from '../../../../api/exchange';

interface T {
	code: string;
	amount: string;
	rtp: number;
	id: number;
}

const TotalAsset = () => {
	const login = useRecoilValue(isLogin);
	const [total, setTotal] = useState(0);
	const myBalance = useRecoilValue(myBalanceState);
	const [myCoins, setMyCoins] = useRecoilState(myCoinsState);
	const coinData = useRecoilValue(coinDataState);
	const [data, setData] = useState<T[]>();
	const getWalletHandler = async () => {
		try {
			const res = await getWallet();
			setMyCoins(res);
		} catch (err) {
			console.log(err);
		}
	};
	const realTimePrice = () => {
		const newData = myCoins.map((v, i) => {
			const p = coinData.filter((c) => c.code === v.code)[0]?.ticker
				?.trade_price as string;
			const obj = {
				id: i,
				code: v.code,
				amount: (+v.amount).toFixed(8),
				rtp: Math.round(+p * +v.amount),
			};
			return obj;
		});
		const total = newData.reduce((a, b) => a + b.rtp, 0);
		setTotal(myBalance + total);
		return newData;
	};
	useEffect(() => {
		if (login) getWalletHandler();
	}, []);
	useEffect(() => {
		if (login && myCoins) {
			const newData = realTimePrice();
			setData(newData);
		}
	}, [coinData]);
	const ratio = (num: number) => {
		if (total) {
			const result = (num / total) * 100;
			return result && result !== Infinity ? result.toFixed(2) + '%' : 0;
		} else return 0;
	};
	return (
		<Wrapper>
			<div className="title">
				총 보유자산
				<span>
					<strong>{total && total.toLocaleString()}</strong>KRW
				</span>
			</div>
			<div className="search-box">
				<AiOutlineSearch className="icon" />
				<input type="text" placeholder="코인명/심볼검색" />
			</div>
			{/* <Table headerGroups={BALANCES_THEAD} bodyDatas={BALANCES_TBODY.data} /> */}

			<table>
				<thead>
					<tr>
						{BALANCES_THEAD.map((v, i) => (
							<th key={i}>{v}</th>
						))}
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>KRW</td>
						<td>{ratio(myBalance)}</td>
						<td>{myBalance && myBalance.toLocaleString()}</td>
					</tr>
					{data &&
						data.map((v) => (
							<tr key={v.id}>
								<td>{v.code}</td>
								<td>{ratio(v.rtp)}</td>
								<td>
									<div>{v.amount}</div>
									<div>{v.rtp && v.rtp.toLocaleString()}</div>
								</td>
							</tr>
						))}
				</tbody>
			</table>
		</Wrapper>
	);
};

export default TotalAsset;
