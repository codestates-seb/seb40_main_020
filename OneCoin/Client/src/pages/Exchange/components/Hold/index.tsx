import React, { useState } from 'react';
import { HoldComponent } from './style';
import {
	proceedsCalculator,
	yieldCalculator,
} from '../../../../utills/calculate';

function Hold() {
	const mainTitleMenu = ['보유 코인', '미체결'];
	const [title, setTitle] = useState('보유 코인');
	const titleClickHandler = (item: string) => setTitle(item);
	const subTitleMenu =
		title === '보유 코인'
			? ['코인명', '보유(평가금)', '매수평균가', '수익률']
			: ['주문시간', '구분', '주문가격', '주문량', '미체결량', '취소'];
	const item = [
		{
			date: '11.15 16:20',
			order: '매수',
			orderPrice: 23800000,
			orderSize: 1.12345678,
			notSignedSize: 0.065742,
		},
		{
			date: '11.15 16:20',
			order: '매도',
			orderPrice: 23800000,
			orderSize: 1.12345678,
			notSignedSize: 0.065742,
		},
	];
	const holdItem = [
		{
			coin: '비트코인',
			hold: { size: 1.12345678, price: 26962962 },
			avgPrice: 23000000,
			yield: { yield: '+0.57%', proceeds: 129000 },
		},
		{
			coin: '코스모스',
			hold: { size: 1.12345678, price: 26962962 },
			avgPrice: 23000000,
			yield: { yield: '+0.57%', proceeds: 129000 },
		},
	];
	return (
		<HoldComponent>
			<div className="hold-menu">
				{mainTitleMenu.map((v, i) => (
					<div
						key={i}
						className={v === title ? 'select' : ''}
						onClick={() => titleClickHandler(v)}
					>
						{v}
					</div>
				))}
			</div>

			<table>
				<thead>
					<tr className="hold-title">
						{subTitleMenu.map((v, i) => (
							<td key={i}>{v}</td>
						))}
					</tr>
				</thead>

				<tbody>
					{title === '보유 코인'
						? holdItem.map((v, i) => (
								<tr key={i} className="hold-item">
									<td>{v.coin}</td>
									<tr>
										<td>{v.hold.size}</td>
										<td>{`${v.hold.price.toLocaleString()} KRW`}</td>
									</tr>
									<td>{v.avgPrice.toLocaleString()}</td>
									<tr className="today-range">
										<td>{v.yield.yield}</td>
										<td>{v.yield.proceeds.toLocaleString()}</td>
									</tr>
								</tr>
						  ))
						: item.map((v, i) => (
								<tr key={i} className="hold-item">
									<td>{v.date}</td>
									<td className={v.order === '매수' ? 'bid' : 'ask'}>
										{v.order}
									</td>
									<td>{v.orderPrice.toLocaleString()}</td>
									<td>{v.orderSize}</td>
									<td>{v.notSignedSize}</td>
									<td className={'cancel'}>취소</td>
								</tr>
						  ))}
				</tbody>
			</table>
		</HoldComponent>
	);
}

export default Hold;
