import StompJs from 'stompjs';
import SockJS from 'sockjs-client';
import { Ticker, OrderBook, CoinDataType } from '../../utills/types';

type T = (
	coinData: CoinDataType[],
	setCoinData: React.Dispatch<React.SetStateAction<CoinDataType[]>>
) => void;

export const connection: T = (coinData, setCoinData) => {
	const socket = new SockJS(
		'https://0be1-124-5-120-166.jp.ngrok.io/ws/upbit-info'
	);
	const client = StompJs.over(socket);
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
