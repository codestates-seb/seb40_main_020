import { CoinDataType } from './types';

export function codeCoin(arr: CoinDataType[], str: string, ts: string) {
	if (!str) return '';
	if (ts === 'coin') return arr.filter((c) => c.code === str)[0].coin;
	else if (ts === 'code') return arr.filter((c) => c.coin === str)[0].code;
	else return str;
}
