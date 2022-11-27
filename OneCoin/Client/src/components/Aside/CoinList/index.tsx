import React, { useState, useEffect, memo } from 'react';
import { CoinListComponent } from './style';
import { AiOutlineSearch } from 'react-icons/ai';
import { coinDataState } from '../../../store';
import { useRecoilState } from 'recoil';
import { Props } from '../index';
import { CoinDataType } from '../../../utills/types';

function CoinList({ coinInfoHandler }: Props) {
	const [coinlist, setCoinlist] = useRecoilState(coinDataState);
	const newcoinlist = [...coinlist];
	const subTitleMenu = ['코인명', '현재가', '전일대비'];
	const coinCodes = coinlist.map((v) => v.code);
	const [searchResult, setSearchResult] = useState<CoinDataType[]>([]);
	const searchInputHandler = (e: React.ChangeEvent<HTMLInputElement>) => {
		const newSearchResult = coinlist.filter((v) =>
			v.coin.includes(e.target.value)
		);
		setSearchResult(newSearchResult);
	};

	useEffect(() => {
		const socket = new WebSocket('wss://api.upbit.com/websocket/v1');
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
		const codes = [
			{ type: 'ticker', codes: coinCodes },
			{ type: 'orderbook', codes: coinCodes },
		];
		socketAPI(codes);
		const coinPriceInterval = setInterval(() => {
			setCoinlist([...newcoinlist]);
		}, 1000);
		return () => clearInterval(coinPriceInterval);
	}, []);

	const isShow = (v: CoinDataType, i: number) => {
		const ticker = v?.ticker;
		const change = ticker?.change;
		const todayRate = change === 'FALL' ? 'fall' : 'rise';
		const tradePrice = ticker?.trade_price ? ticker.trade_price : 0;
		const changeRate = ticker?.signed_change_rate
			? ticker.signed_change_rate
			: 0;
		const changePrice = ticker?.signed_change_price
			? ticker.signed_change_price
			: 0;
		const newCoinInfo = { coin: v.coin, code: v.code, symbol: v.symbol };

		return (
			<tr key={i} onClick={() => coinInfoHandler(newCoinInfo)}>
				<td>{v.coin}</td>
				<td className={todayRate}>{tradePrice.toLocaleString()}</td>
				<td className={todayRate}>
					<div>
						<span>{(changeRate * 100).toFixed(2) + '%'}</span>
						<span>{changePrice.toLocaleString()}</span>
					</div>
				</td>
			</tr>
		);
	};
	return (
		<CoinListComponent>
			<div className="coin-search">
				<input
					type="text"
					placeholder="코인명 검색"
					onChange={searchInputHandler}
				/>
				<div className="serach-icon">
					<AiOutlineSearch />
				</div>
			</div>
			<table className="sub-title">
				<thead>
					<tr>
						{subTitleMenu.map((v, i) => (
							<th key={i}>{v}</th>
						))}
					</tr>
				</thead>
				<tbody>
					{searchResult.length
						? searchResult.map(isShow)
						: coinlist.map(isShow)}
				</tbody>
			</table>
		</CoinListComponent>
	);
}

export default memo(CoinList);
