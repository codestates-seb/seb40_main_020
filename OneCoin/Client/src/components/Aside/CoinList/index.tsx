import React, { useState, useEffect } from 'react';
import { CoinListComponent } from './style';
import { AiOutlineSearch } from 'react-icons/ai';
import { coinDataState } from '../../../store';
import { useRecoilState } from 'recoil';
import { Props } from '../index';

function CoinList({ symbolHandler }: Props) {
	const [coinlist, setCoinlist] = useRecoilState(coinDataState);
	const newcoinlist = [...coinlist];
	const subTitleMenu = ['코인명', '현재가', '전일대비'];
	const socket = new WebSocket('wss://api.upbit.com/websocket/v1');
	const coinCodes = coinlist.map((v) => v.code);

	function socketAPI(codes: { type: string; codes: string[] }[]) {
		const data = [{ ticket: 'test' }, ...codes];
		socket.addEventListener('open', function (event) {
			socket.send(JSON.stringify(data));
		});
		socket.addEventListener('message', function (event) {
			event.data.text().then((res: string) => {
				const data = JSON.parse(res);
				const idx = coinCodes.indexOf(data.code);
				// console.log(data);
				if (data.type === 'ticker') newcoinlist[idx].ticker = data;
				if (data.type === 'orderbook') newcoinlist[idx].orderbook = data;
			});
		});
		socket.addEventListener('error', (event) => {
			console.log(event);
		});
	}

	useEffect(() => {
		const codes = [
			{ type: 'ticker', codes: coinCodes },
			{ type: 'orderbook', codes: coinCodes },
		];
		socketAPI(codes);
	}, []);

	useEffect(() => {
		const coinPriceInterval = setInterval(() => {
			setCoinlist([...newcoinlist]);
		}, 1000);
		return () => clearInterval(coinPriceInterval);
	}, []);

	return (
		<CoinListComponent>
			<div className="coin-search">
				<input type="text" placeholder="코인명 검색" />
				<div className="serach-icon">
					<AiOutlineSearch />
				</div>
			</div>
			<table className="sub-title">
				<thead>
					<tr>
						{subTitleMenu.map((v, i) => (
							<td key={i}>{v}</td>
						))}
					</tr>
				</thead>
				<tbody>
					{coinlist &&
						coinlist.map((v, i) => {
							const change = v?.ticker?.change;
							let todayRate = '';
							if (change === 'FALL') todayRate = 'fall';
							else if (change === 'RISE') todayRate = 'rise';
							return (
								<tr
									key={i}
									onClick={() =>
										symbolHandler({
											coin: v.coin,
											code: v.code,
											symbol: v.symbol,
										})
									}
								>
									<td>{v.coin}</td>
									<td className={todayRate}>
										{v?.ticker?.trade_price
											? v.ticker.trade_price.toLocaleString()
											: 0}
									</td>
									<tr className={todayRate}>
										<td>
											{v?.ticker?.signed_change_rate
												? `${(v.ticker.signed_change_rate * 100).toFixed(2)}%`
												: '0%'}
										</td>
										<td>
											{v?.ticker?.signed_change_price
												? v.ticker.signed_change_price.toLocaleString()
												: 0}
										</td>
									</tr>
								</tr>
							);
						})}
				</tbody>
			</table>
		</CoinListComponent>
	);
}

export default CoinList;
