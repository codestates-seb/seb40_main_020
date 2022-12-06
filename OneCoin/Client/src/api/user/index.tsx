import api from '../index';

export const userLogin = {
	login: async (data: any) => {
		const response = await api.post('/api/auth/login', data);
		return response.headers;
	},
};
