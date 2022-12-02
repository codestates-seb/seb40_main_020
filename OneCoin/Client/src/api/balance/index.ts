import axios from 'axios';
import { SERVER_URL } from '../';
export const getBalance = async () => {
	try {
		const Authorization = sessionStorage.getItem('login-token') as string;
		const headers = { Authorization };
		const res = await axios.get(`${SERVER_URL}/api/balances`, { headers });
		console.log(res);
		return res.data.data.balance;
	} catch (err) {
		console.log(err);
	}
};

export const postDeposit = async (data: { depositAmount: number }) => {
	try {
		const Authorization = sessionStorage.getItem('login-token') as string;
		const headers = { Authorization };
		const res = await axios.post(`${SERVER_URL}/api/deposits`, data, {
			headers,
		});
		return res.data.data;
	} catch (err) {
		console.log(err);
	}
};

export const getDeposits = async (page: number) => {
	try {
		const Authorization = sessionStorage.getItem('login-token') as string;
		const headers = { Authorization };
		const res = await axios.get(
			`${SERVER_URL}/api/deposits?page=${page}&size=10`,
			{
				headers,
			}
		);
		return res.data;
	} catch (err) {
		console.log(err);
	}
};
