import axios from 'axios';

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
