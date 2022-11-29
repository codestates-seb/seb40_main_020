import StompJs from 'stompjs';
import SockJS from 'sockjs-client';
import { Ticker, OrderBook, CoinDataType } from '../../utills/types';
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
