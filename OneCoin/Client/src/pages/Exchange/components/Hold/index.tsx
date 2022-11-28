import Radio from 'components/Radio';
import React, { useState, useEffect, memo } from 'react';
import { HoldComponent } from './style';
import {
	myCoinsTitleMenu,
	completeOrdersTitleMenu,
	nonTradingOdersTitleMenu,
} from '../../../../utills/constants/exchange';
import {
	MyCoins,
	CompleteOrders,
	NonTradingOders,
} from '../../../../utills/types';
const MY_COIN_DATA = [
	{
		code: 'KRW-BTC',
		amount: '1.12345678',
		priceEvaluation: '26962962',
		averagePrice: '23000000',
		change: 'FALL',
		changeRate: '-0.57%',
		changePrice: '129000',
	},
	{
		code: 'KRW-ATOM',
		amount: '1.12345678',
		priceEvaluation: '26962962',
		averagePrice: '23000000',
		change: 'RISE',
		changeRate: '+0.57%',
		changePrice: '129000',
	},
];
const NON_TRADING_ORDERS = [
	{
		code: 'KRW-BTC',
		orderTime: '11.15 16:20',
		orderType: '매수',
		limit: '',
		market: '',
		stopLimit: '',
		amount: '',
	},
	{
		code: 'KRW-BTC',
		orderTime: '11.15 16:20',
		orderType: '매도',
		limit: '',
		market: '',
		stopLimit: '',
		amount: '',
	},
];
const COMPLETE_ORDERS = [
	{
		code: 'KRW-BTC',
		completedTime: '11.15 16:20',
		orderType: '매수',
		price: '',
		amount: '',
	},
	{
		code: 'KRW-BTC',
		completedTime: '11.15 16:20',
		orderType: '매도',
		price: '',
		amount: '',
	},
];
interface Props {
	title: string;
}

function Hold({ title }: Props) {
	const [myCoins, setMyCoins] = useState<MyCoins[]>([]);
	const [completeOrders, setCompleteOrders] = useState<CompleteOrders[]>([]);
	const [nonTradingOders, setNonTradingOrers] = useState<NonTradingOders[]>([]);
	const [complete, setComplete] = useState('non-trading');

	useEffect(() => {
		setTimeout(() => {
			setMyCoins(MY_COIN_DATA);
			setNonTradingOrers(NON_TRADING_ORDERS);
			setCompleteOrders(COMPLETE_ORDERS);
		}, 1);
	}, []);

	const drawTitle = (v: string, i: number) => <td key={i}>{v}</td>;
	const radioClickHandler = (value: string) => setComplete(value);
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
						? myCoins.map((v, i) => (
								<tr key={i} className="hold-item">
									<td>{v.code as string}</td>
									<td>
										<div>{v.amount as string}</div>
										<div>{`${v.priceEvaluation as string} KRW`}</div>
									</td>
									<td>{v.averagePrice as string}</td>
									<td
										className={(v.change as string) === 'FALL' ? 'ask' : 'bid'}
									>
										<div>{v.changeRate as string}</div>
										<div>{v.changePrice as string}</div>
									</td>
								</tr>
						  ))
						: complete === 'non-trading'
						? nonTradingOders.map((v, i) => (
								<tr key={i} className="hold-item">
									<td>{v.code as string}</td>
									<td
										className={
											(v.orderType as string) === '매수' ? 'bid' : 'ask'
										}
									>
										{v.orderType as string}
									</td>
									<td>{v.limit as string}</td>
									<td>{v.stopLimit as string}</td>
									<td>{v.amount as string}</td>
									<td className={'cancel'}>취소</td>
								</tr>
						  ))
						: completeOrders.map((v, i) => (
								<tr key={i}>
									<td>{v.code as string}</td>
									<td>{v.completedTime as string}</td>
									<td
										className={
											(v.orderType as string) === '매수' ? 'bid' : 'ask'
										}
									>
										{v.orderType as string}
									</td>
									<td>{v.price as string}</td>
									<td>{v.amount as string}</td>
								</tr>
						  ))}
				</tbody>
			</table>
		</HoldComponent>
	);
}

export default memo(Hold);
