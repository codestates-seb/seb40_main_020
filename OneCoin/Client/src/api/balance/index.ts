import api from '..';

export const getBalance = async () => {
	try {
		const res = await api.get(`/api/balances`);
		return res.data.data.balance;
	} catch (err) {
		console.log(err);
	}
};

export const postDeposit = async (data: { depositAmount: number }) => {
	try {
		const res = await api.post(`/api/deposits`, data);
		return res.data.data;
	} catch (err) {
		console.log(err);
	}
};

export const getDeposits = async (page: number) => {
	try {
		const res = await api.get(`/api/deposits?page=${page}&size=10`);
		return res.data;
	} catch (err) {
		console.log(err);
	}
};
