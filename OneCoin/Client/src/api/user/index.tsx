import axios from 'axios';

const api = axios.create({
	baseURL: process.env.REACT_APP_SERVER_URL,
});

api.interceptors.request.use(function (config: any) {
	const accessToken = sessionStorage.getItem('login-token');
	const refreshToken = sessionStorage.getItem('login-refresh');

	if (!accessToken && !refreshToken) {
		config.headers['accessToken'] = null;
		config.headers['refreshToken'] = null;
		return config;
	}

	if (config.headers && accessToken && refreshToken) {
		config.headers['Authorization'] = accessToken.replace(/\"/gi, '');
		config.headers['refreshToken'] = refreshToken.replace(/\"/gi, '');
		return config;
	}
});

api.interceptors.response.use(
	function (response) {
		return response;
	},
	async function (err) {
		const originalConfig = err.config;

		if (err.response && err.response.status === 403) {
			const accessToken = originalConfig.headers['Authorization'];
			const refreshToken = originalConfig.headers['refreshToken'];
			try {
				const data = await axios({
					url: `${process.env.REACT_APP_SERVER_URL}/api/auth/login`,
					method: 'GET',
					headers: {
						Authorization: accessToken,
						refreshToken: refreshToken,
					},
				});
				if (data) {
					sessionStorage.setItem(
						'token',
						JSON.stringify(data.headers.authorization).replace(/\"/gi, '')
					);
					sessionStorage.setItem(
						'refreshToken',
						JSON.stringify(data.headers.refreshtoken).replace(/\"/gi, '')
					);
					return await api.request(originalConfig);
				}
			} catch (err) {
				console.log('토큰 갱신 에러');
			}
			return Promise.reject(err);
		}
		return Promise.reject(err);
	}
);
export default api;

export const userLogin = {
	login: async (data: any) => {
		const response = await api.post('/api/auth/login', data);
		return response.headers;
	},
};
