import React, { useState, useEffect, memo } from 'react';
import { CoinListComponent } from './style';
import { AiOutlineSearch } from 'react-icons/ai';
import {
	coinDataState,
	sessionIdState,
	userIdState,
	isCoinSocket,
} from '../../../store';
import { useRecoilState } from 'recoil';
import { Props } from '../index';
import { CoinDataType } from '../../../utills/types';
import { connect, coinDataSubscribe } from 'api/socket';
import { useLocation } from 'react-router-dom';

function CoinList({ coinInfoHandler, isLeftSidebar }: Props) {
	const [coinData, setCoinData] = useRecoilState(coinDataState);
	const subTitleMenu = ['코인명', '현재가', '전일대비'];
	const [searchResult, setSearchResult] = useState<CoinDataType[]>([]);
	const [sessionId, setSessionId] = useRecoilState(sessionIdState);
	const [userId, setUserId] = useRecoilState(userIdState);
	const [isCoinSocketConnect, setIsCoinSocketConnect] =
		useRecoilState(isCoinSocket);
	const searchInputHandler = (e: React.ChangeEvent<HTMLInputElement>) => {
		const newSearchResult = coinData.filter((v) =>
			v.coin.includes(e.target.value)
		);
		setSearchResult(newSearchResult);
	};
	const { pathname } = useLocation();
	const log = ['/login', '/signup', '/findpassword'].includes(pathname);
	const socketConnect = () => {
		if (!sessionId && !log) {
			connect(setSessionId, setUserId);
		}
	};
	const socketSub = () => {
		if (isLeftSidebar && !isCoinSocketConnect && sessionId) {
			coinDataSubscribe(coinData, setCoinData);
			setIsCoinSocketConnect(true);
		}
	};
	useEffect(() => {
		socketConnect();
	}, []);
	useEffect(() => {
		const t = async () => {
			await socketConnect();
			await socketSub();
		};
		t();
	}, [sessionId]);

	const isShow = (v: CoinDataType, i: number) => {
		const ticker = v?.ticker;
		const change = ticker?.change;
		const todayRate = change === 'FALL' ? 'fall' : 'rise';
		const tradePrice = ticker?.trade_price ? ticker.trade_price : 0;
		const changeRate = ticker?.change_rate ? ticker.change_rate : 0;
		const changePrice = ticker?.change_price ? ticker.change_price : 0;
		const newCoinInfo = { coin: v.coin, code: v.code, symbol: v.symbol };
		return (
			<tr key={i} onClick={() => coinInfoHandler(newCoinInfo)}>
				<td>{v.coin}</td>
				<td className={todayRate}>{Number(tradePrice).toLocaleString()}</td>
				<td className={todayRate}>
					<div>
						<span>
							{change === 'FALL' ? '-' : ''}
							{(Number(changeRate) * 100).toFixed(2) + '%'}
						</span>
						<span>
							{change === 'FALL' ? '-' : ''}
							{Number(changePrice).toLocaleString()}
						</span>
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
						: coinData.map(isShow)}
				</tbody>
			</table>
		</CoinListComponent>
	);
}

export default memo(CoinList);
