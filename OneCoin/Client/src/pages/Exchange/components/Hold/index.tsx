import Radio from 'components/Radio';
import React, { useState, useEffect, memo } from 'react';
import { HoldComponent } from './style';
import {
	myCoinsTitleMenu,
	completeOrdersTitleMenu,
	nonTradingOdersTitleMenu,
} from '../../../../utills/constants/exchange';
import { MyCoins, CompleteOrders } from '../../../../utills/types';

import {
	getNonTrading,
	getWallet,
	getCompleteTrade,
	deleteOrder,
} from '../../../../api/exchange';
import {
	proceedsCalculator,
	rateCalculator,
	dateCalc,
} from '../../../../utills/calculate';
import { useRecoilValue, useRecoilState } from 'recoil';
import {
	coinDataState,
	coinInfoState,
	nonTradingOdersState,
	myCoinsState,
	isLogin,
} from '../../../../store';

interface Props {
	title: string;
}

function Hold({ title }: Props) {
	const login = useRecoilValue(isLogin);
	const [myCoins, setMyCoins] = useRecoilState(myCoinsState);
	const [completeOrders, setCompleteOrders] = useState<CompleteOrders[]>([]);
	const [nonTradingOrders, setNonTradingOrders] =
		useRecoilState(nonTradingOdersState);
	const [complete, setComplete] = useState('non-trading');
	const coinData = useRecoilValue(coinDataState);
	const coinInfo = useRecoilValue(coinInfoState);
	const getNonTradingData = async () => {
		try {
			const res = await getNonTrading();
			setNonTradingOrders(res);
		} catch (err) {
			console.log(err);
		}
	};
	const getCompleteOrderData = async (code: string) => {
		try {
			const res = await getCompleteTrade(code);
			setCompleteOrders(res);
		} catch (err) {
			console.log(err);
		}
	};
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
			const change = rate > 0 ? 'rise' : rate < 0 ? 'fall' : '';
			const priceEvaluation = Math.round(+realTimePrice * amount);
			const obj = {
				...v,
				change,
				changePrice: proceeds ? proceeds.toLocaleString() : '0',
				changeRate: rate + '%',
				priceEvaluation: priceEvaluation
					? priceEvaluation.toLocaleString()
					: '0',
			};
			return obj;
		});
	};

	useEffect(() => {
		if (login && myCoins) {
			const newMyCoins = realTimeCalculator(myCoins);
			setMyCoins(newMyCoins);
		}
	}, [coinData]);
	useEffect(() => {
		if (login) {
			getNonTradingData();
			getWalletData();
			getCompleteOrderData(coinInfo.code);
		}
	}, []);

	const drawTitle = (v: string, i: number) => <td key={i}>{v}</td>;
	const radioClickHandler = (value: string) => setComplete(value);
	const deleteOrderHandler = (orderId: number) => {
		deleteOrder(orderId);
		getNonTradingData();
	};
	return (
		<HoldComponent>
			<table>
				<thead>
					{title !== '보유 코인' ? (
						<tr className="trade-status">
							<td>
								<Radio
									value="non-trading"
									name="trade"
									defaultChecked={true}
									onClick={radioClickHandler}
									complete={complete}
								>
									미체결
								</Radio>
								<Radio
									value="complete"
									name="trade"
									onClick={radioClickHandler}
									complete={complete}
								>
									체결
								</Radio>
							</td>
						</tr>
					) : (
						<></>
					)}
					<tr className="hold-title">
						{title === '보유 코인'
							? myCoinsTitleMenu.map(drawTitle)
							: complete === 'non-trading'
							? nonTradingOdersTitleMenu.map(drawTitle)
							: completeOrdersTitleMenu.map(drawTitle)}
					</tr>
				</thead>

				<tbody>
					{title === '보유 코인'
						? myCoins &&
						  login &&
						  myCoins.map((v, i) => (
								<tr key={i} className="hold-item">
									<td>{v.code as string}</td>

									<td>
										{Math.round(
											Number(v.averagePrice as string)
										).toLocaleString()}
									</td>
									<td>
										<div>{Number(v.amount as string).toFixed(8)}</div>
										<div
											className={
												(v.change as string) === 'fall' ? 'ask' : 'bid'
											}
										>
											{v.priceEvaluation as string}
										</div>
									</td>
									<td
										className={(v.change as string) === 'fall' ? 'ask' : 'bid'}
									>
										<div>{v.changeRate as string}</div>
										<div>{v.changePrice as string}</div>
									</td>
								</tr>
						  ))
						: complete === 'non-trading'
						? nonTradingOrders &&
						  login &&
						  nonTradingOrders.map((v, i) => (
								<tr key={i} className="hold-item">
									<td>{v.code as string}</td>
									<td>{dateCalc(v.orderTime as string)}</td>
									<td
										className={
											(v.orderType as string) === 'BID' ? 'bid' : 'ask'
										}
									>
										{(v.orderType as string) === 'BID' ? '매수' : '매도'}
									</td>
									<td>{(+(v.limit as string)).toLocaleString()}</td>
									<td>{v.amount as string}</td>
									<td
										className={'cancel'}
										onClick={() => {
											deleteOrderHandler(v.orderId);
										}}
									>
										취소
									</td>
								</tr>
						  ))
						: completeOrders &&
						  completeOrders.map((v, i) => (
								<tr key={i}>
									<td>{v.code as string}</td>
									<td>{dateCalc(v.completedTime)}</td>
									<td
										className={
											(v.orderType as string) === 'BID' ? 'bid' : 'ask'
										}
									>
										{(v.orderType as string) === 'BID' ? '매수' : '매도'}
									</td>
									<td>{Number(v.price as string).toLocaleString()}</td>
									<td>{Number(v.amount as string).toFixed(8)}</td>
								</tr>
						  ))}
				</tbody>
			</table>
		</HoldComponent>
	);
}

export default memo(Hold);
