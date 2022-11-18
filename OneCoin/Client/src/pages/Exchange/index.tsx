import React, { useEffect, useState } from 'react';
import Chart from './components/Chart';
import { useRecoilValue } from 'recoil';
import { coinDataState } from '../../store';
import QuoteList from './components/QuoteList';
import { ExchangeComponent } from './style';
import { GoTriangleUp, GoTriangleDown } from 'react-icons/go';
import Order from './components/Order';
import Aside from 'components/Aside';
import HoldList from './components/HoldList';

function Exchange() {
	interface T {
		coin: string;
		code: string;
		symbol: string;
	}
	const coinData = useRecoilValue(coinDataState);
	const [symbol, setSymbol] = useState<T>({
		coin: '비트코인',
		code: 'KRW-BTC',
		symbol: 'BTCKRW',
	});
	const [coin, setCoin] = useState(
		coinData.filter((v) => v.coin === symbol.coin)[0]
	);
	const symbolHandler = (item: T) => setSymbol(item);

	const [inputPrice, setInputPrice] = useState<number>(0);
	const prcieClickHandler = (price: number) => setInputPrice(price);
	useEffect(() => {
		const newData = coinData.filter((v) => v.coin === symbol.coin)[0];
		setCoin(newData);
		setInputPrice(newData.ticker?.trade_price ? newData.ticker.trade_price : 0);
	}, [symbol]);
	useEffect(() => {
		const newData = coinData.filter((v) => v.coin === symbol.coin)[0];
		setInputPrice(newData.ticker?.trade_price ? newData.ticker.trade_price : 0);
	}, []);
	return (
		<ExchangeComponent todayChange={coin?.ticker?.change && coin.ticker.change}>
			<div className="coin-title">
				<h1>{coin.coin}</h1>
				<div>
					<h2 className="current-price today-range">
						{coin?.ticker?.trade_price
							? coin.ticker.trade_price.toLocaleString()
							: ''}
					</h2>
					<div className="today-price">
						<span>전일대비</span>
						<span className="today-range">
							{coin?.ticker?.signed_change_rate
								? (coin.ticker.signed_change_rate * 100).toFixed(2) + ' %'
								: ''}
						</span>
						<span>
							{coin?.ticker?.change && coin.ticker.change === 'RISE' ? (
								<GoTriangleUp className="today-range" />
							) : coin?.ticker?.change && coin.ticker.change === 'FALL' ? (
								<GoTriangleDown className="today-range" />
							) : (
								<></>
							)}
						</span>
						<span className="today-range">
							{coin?.ticker?.signed_change_price
								? coin.ticker.signed_change_price.toLocaleString()
								: ''}
						</span>
					</div>
				</div>
			</div>
			<div className="chart-wrapper">
				<Chart symbol={symbol.symbol} />
			</div>
			<div className="quote-wrapper">
				<QuoteList
					coinOrderbook={coin?.orderbook && coin.orderbook}
					prcieClickHandler={prcieClickHandler}
					tradePrice={coin?.ticker?.trade_price && coin.ticker.trade_price}
				/>
			</div>
			<div className="order-wrapper">
				<Order inputPrice={inputPrice} setInputPrice={setInputPrice} />
			</div>
			<div className="hold-wrapper">
				<HoldList />
			</div>
			<div className="aside-wrapper">
				<Aside symbolHandler={symbolHandler} />
			</div>
		</ExchangeComponent>
	);
}

export default Exchange;
