import { atom } from 'recoil';
import { CoinDataType } from '../utills/types';

const symbolState = atom({
	key: 'symbolState',
	default: { coin: '비트코인', symbol: 'KRW-BTC' },
});
const coinDataState = atom<CoinDataType[]>({
	key: 'coinOrderbookState',
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
			coin: '코스모스',
			code: 'KRW-ATOM',
			symbol: 'ATOMKRW',
		},
		{
			coin: '리플',
			code: 'KRW-XRP',
			symbol: 'XRPKRW',
		},
		{
			coin: '도지',
			code: 'KRW-DOGE',
			symbol: 'DOGEKRW',
		},
	],
	dangerouslyAllowMutability: true,
});

const isLogin = atom({
	key: 'isLogin',
	default: false,
});

export { symbolState, coinDataState, isLogin };
