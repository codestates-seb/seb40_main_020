import StompJs from 'stompjs';
import SockJS from 'sockjs-client';
import { Ticker, OrderBook, CoinDataType, Trade } from '../../utills/types';
import { SERVER_URL } from '../';
import axios from 'axios';

type T = (
	coinData: CoinDataType[],
	setCoinData: React.Dispatch<React.SetStateAction<CoinDataType[]>>
) => void;

let client: StompJs.Client;

export const getTradeData: T = (coinData, setCoinData) => {
	const socket = new SockJS(`${SERVER_URL}/ws/upbit-info`);
	client = StompJs.over(socket);
	client.debug = function () {
		return;
	};
	client.connect({}, () => {
		setTimeout(() => {
			client.subscribe('/info/upbit', (msg) => {
				const data = JSON.parse(msg.body).data;
				const newCoinData = [...coinData].map((v) => {
					const ticker = data.ticker.filter(
						(tic: Ticker) => tic.code === v.code
					)[0];
					const orderBook = data.orderBook.filter(
						(order: OrderBook) => order.code === v.code
					)[0];
					const obj = { ...v, ticker, orderBook };
					return obj;
				});
				setCoinData(newCoinData);
			});
		}, 500);
	});
};

export const disconnection = () => {
	client.disconnect(() => console.log('disconnect'));
};

export const stopTradeData = () => {
	client.unsubscribe('/info/upbit');
};
export async function getChartData(time: number, code: string, date: string) {
	try {
		const res = await axios.get(
			`https://api.upbit.com/v1/candles/minutes/${time}?market=${code}&count=200&to=${date}`
		);
		return res.data;
	} catch (err) {
		console.log(err);
	}
}

export const getNonTrading = async () => {
	try {
		const Authorization = sessionStorage.getItem('login-token') as string;
		const headers = { Authorization };
		const res = await axios.get(`${SERVER_URL}/api/order/non-trading`, {
			headers,
		});
		return res.data.data;
	} catch (err) {
		console.log(err);
	}
};

export const getWallet = async () => {
	try {
		const Authorization = sessionStorage.getItem('login-token') as string;
		const headers = { Authorization };
		const res = await axios.get(`${SERVER_URL}/api/order/my-coin`, { headers });
		return res.data.data;
	} catch (err) {
		console.log(err);
	}
};

export const getCompleteTrade = async (code: string) => {
	try {
		const Authorization = sessionStorage.getItem('login-token') as string;
		const headers = { Authorization };
		const res = await axios.get(`${SERVER_URL}/api/order/completion/${code}`, {
			headers,
		});
		return res.data.data;
	} catch (err) {
		console.log(err);
	}
};

export const getCompleteTradePage = async (
	page: number,
	period: string,
	type: string,
	code?: string
) => {
	try {
		const Authorization = sessionStorage.getItem('login-token') as string;
		const headers = { Authorization };
		const c = (code as string) ? `&code=${code}` : '';
		const res = await axios.get(
			`${SERVER_URL}/api/order/completion?period=${period}&type=${type}&page=${page}${c}`,
			{ headers }
		);
		console.log(
			`${SERVER_URL}/api/order/completion?period=${period}&type=${type}&page=${page}`,
			res.data
		);
		return res.data;
	} catch (err) {
		console.log(err);
	}
};

export const postOrder = async (code: string, data: Trade) => {
	try {
		const Authorization = sessionStorage.getItem('login-token') as string;
		const headers = { Authorization };
		const res = await axios.post(`${SERVER_URL}/api/order/${code}`, data, {
			headers,
		});
		return res.data;
	} catch (err) {
		console.log(err);
	}
};
export const deleteOrder = async (orderId: number) => {
	try {
		const Authorization = sessionStorage.getItem('login-token') as string;
		const headers = { Authorization };
		const res = await axios.delete(
			`${SERVER_URL}/api/order/non-trading/${orderId}`,
			{
				headers,
			}
		);
		return res;
	} catch (err) {
		console.log(err);
	}
};
