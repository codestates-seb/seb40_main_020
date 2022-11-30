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
import Layout from '../../components/Layout';
import ChartList from './components/ChartList';
import { CoinInfo, CoinDataType } from '../../utills/types';

function Exchange() {
	const COIN_INFO_INITIAL_DATA = {
		coin: '비트코인',
		code: 'KRW-BTC',
		symbol: 'BTCKRW',
	};
	const coinData = useRecoilValue(coinDataState);
	const [coinInfo, setCoinInfo] = useState<CoinInfo>(COIN_INFO_INITIAL_DATA);
	const [coin, setCoin] = useState<CoinDataType>();
	const coinInfoHandler = (item: CoinInfo) => setCoinInfo(item);
	const [inputPrice, setInputPrice] = useState<number>(0);
	const prcieClickHandler = (price: number) => setInputPrice(price);
	useEffect(() => {
		const newData = coinData.filter((v) => v.coin === coinInfo.coin)[0];
		const price = newData?.ticker?.trade_price
			? newData.ticker.trade_price
			: '0';
		setCoin(newData);
		setInputPrice(+price);
	}, [coinInfo]);
	useEffect(() => {
		const newData = coinData.filter((v) => v.coin === coinInfo.coin)[0];
		const price = newData?.ticker?.trade_price
			? newData.ticker.trade_price
			: '0';
		setInputPrice(+price);
	}, []);
	useEffect(() => {
		setCoin(coinData.filter((v) => v?.ticker?.code === coinInfo.code)[0]);
	}, [coinData]);
	const tradePrice = coin?.ticker?.trade_price as string;
	const changeRate = coin?.ticker?.change_rate as string;
	const changePrice = coin?.ticker?.change_price as string;
	const change = coin?.ticker?.change as string;
	console.log(coinData);
	return (
		<Layout isLeftSidebar={false} isLeftMargin={false}>
			<ExchangeComponent
				todayChange={coin?.ticker?.change && coin.ticker.change}
			>
				<div className="coin-title">
					<h1>{coin?.coin}</h1>
					<div>
						<h2 className="current-price today-range">
							{(+tradePrice).toLocaleString()}
						</h2>
						<div className="today-price">
							<span>전일대비</span>
							<span className="today-range">
								{(Number(changeRate) * 100).toFixed(2)}
							</span>
							<span>
								{change === 'RISE' ? (
									<GoTriangleUp className="today-range" />
								) : change === 'FALL' ? (
									<GoTriangleDown className="today-range" />
								) : (
									<></>
								)}
							</span>
							<span className="today-range">
								{Number(changePrice).toLocaleString()}
							</span>
						</div>
					</div>
				</div>

				<ChartList coinInfo={coinInfo} />
				<QuoteList
					orderBook={coin?.orderBook && coin.orderBook}
					prcieClickHandler={prcieClickHandler}
					tradePrice={coin?.ticker?.trade_price}
				/>

				<Order
					inputPrice={inputPrice}
					setInputPrice={setInputPrice}
					coinInfo={coinInfo}
				/>
				<HoldList />
				<div className="aside-wrapper">
					<Aside coinInfoHandler={coinInfoHandler} isLeftSidebar={true} />
				</div>
			</ExchangeComponent>
		</Layout>
	);
}

export default Exchange;
