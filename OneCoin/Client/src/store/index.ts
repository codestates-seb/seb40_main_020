import { atom } from 'recoil';
import {
	CoinDataType,
	CoinInfo,
	MyCoins,
	NonTradingOders,
} from '../utills/types';

const coinInfoState = atom<CoinInfo>({
	key: 'coinInfo',
	default: {
		coin: '비트코인',
		code: 'KRW-BTC',
		symbol: 'BTCKRW',
	},
});
const coinDataState = atom<CoinDataType[]>({
	key: 'coinDataState',
	default: [
		{
			coin: '비트코인',
			code: 'KRW-BTC',
			symbol: 'BTCKRW',
		},
		{
			coin: '이더리움',
			code: 'KRW-ETH',
			symbol: 'ETHKRW',
		},
		{
			coin: '이더리움클래식',
			code: 'KRW-ETC',
			symbol: 'ETCKRW',
		},
		{
			coin: '리플',
			code: 'KRW-XRP',
			symbol: 'XRPKRW',
		},
		{
			coin: '도지코인',
			code: 'KRW-DOGE',
			symbol: 'DOGEKRW',
		},
	],
	dangerouslyAllowMutability: true,
});

const myCoinsState = atom<MyCoins[]>({
	key: 'myCoinsState',
	default: [],
});

const nonTradingOdersState = atom<NonTradingOders[]>({
	key: 'nonTradingOdersState',
	default: [],
});

const myBalanceState = atom({
	key: 'myBalanceState',
	default: 0,
});

const isLogin = atom({
	key: 'isLogin',
	default: false,
});

const sessionIdState = atom({
	key: 'sessionIdState',
	default: '',
});

const userIdState = atom<null | number>({
	key: 'userIdState',
	default: null,
});

const isCoinSocket = atom({
	key: 'isCoinSocket',
	default: false,
});

export {
	coinInfoState,
	coinDataState,
	isLogin,
	nonTradingOdersState,
	myCoinsState,
	myBalanceState,
	sessionIdState,
	userIdState,
	isCoinSocket,
};
