import React, { useState, useEffect } from 'react';
import { AsideComponent } from './style';
import { AiOutlineSearch } from 'react-icons/ai';
import { coinSocketState, coinDataState } from '../../store';
import { useRecoilValue, useRecoilState } from 'recoil';

interface Props {
	symbolHandler: (item: { coin: string; code: string; symbol: string }) => void;
	isLeftSidebar?: boolean;
}

function Aside({ symbolHandler, isLeftSidebar }: Props) {
	const mainTitleMenu = ['코인', '랭킹'];
	const [title, setTitle] = useState('코인');
	const titleClickHandler = (item: string) => setTitle(item);
	const subTitleMenu = ['코인명', '현재가', '전일대비'];

	const [coinlist, setCoinlist] = useRecoilState(coinDataState);
	const newcoinlist = [...coinlist];
	const socket = useRecoilValue(coinSocketState);
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
				console.log(data);
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

		const coinPriceInterval = setInterval(() => {
			setCoinlist(newcoinlist);
		}, 1000);
		return () => clearInterval(coinPriceInterval);
	}, []);

	return (
		<AsideComponent isShow={isLeftSidebar ?? false}>
			<div className="aside-title">
				{mainTitleMenu.map((v, i) => (
					<div
						key={i}
						onClick={() => titleClickHandler(v)}
						className={title === v ? 'select' : ''}
					>
						{v}
					</div>
				))}
			</div>
			{title === '코인' ? (
				<div>
					{/* title이 코인 일때 */}
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
								coinlist.map((v, i) => (
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
										<td className={'today-range'}>
											{v?.ticker?.trade_price
												? v.ticker.trade_price.toLocaleString()
												: 0}
										</td>
										<tr className={'today-range'}>
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
								))}
						</tbody>
					</table>
				</div>
			) : (
				<div>랭킹</div>
			)}
		</AsideComponent>
	);
}

export default Aside;
