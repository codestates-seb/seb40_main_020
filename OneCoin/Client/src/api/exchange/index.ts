import { Trade } from '../../utills/types';
import axios from 'axios';
import api from '..';

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
		const res = await api.get(`/api/order/non-trading`);
		return res.data.data;
	} catch (err) {
		console.log(err);
	}
};

export const getWallet = async () => {
	try {
		const res = await api.get(`/api/order/my-coin`);
		return res.data.data;
	} catch (err) {
		console.log(err);
	}
};

export const getCompleteTrade = async (code: string) => {
	try {
		const res = await api.get(`/api/order/completion/${code}`);
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
		const c = (code as string) ? `&code=${code}` : '';
		const res = await api.get(
			`/api/order/completion?period=${period}&type=${type}&page=${page}${c}`
		);
		return res.data;
	} catch (err) {
		console.log(err);
	}
};

export const postOrder = async (code: string, data: Trade) => {
	try {
		const res = await api.post(`/api/order/${code}`, data);
		return res.data;
	} catch (err) {
		console.log(err);
	}
};
export const deleteOrder = async (orderId: number) => {
	try {
		const res = await api.delete(`/api/order/non-trading/${orderId}`);
		return res;
	} catch (err) {
		console.log(err);
	}
};
