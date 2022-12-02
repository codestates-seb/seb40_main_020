import axios from 'axios';

const api = axios.create({
	baseURL: process.env.REACT_APP_SERVER_URL,
});

//요청시 AccessToken 계속 보내주기
api.interceptors.request.use(function (config: any) {
	const accessToken = sessionStorage.getItem('login-token');
	const refreshToken = sessionStorage.getItem('login-refresh');

	if (!accessToken && !refreshToken) {
		config.headers['Authorization'] = null;
		config.headers['refresh'] = null;
		return config;
	}

	if (config.headers && accessToken && refreshToken) {
		config.headers['Authorization'] = `${accessToken}`;
		config.headers['refresh'] = `${refreshToken}`;
		return config;
	}
});

// AccessToken이 만료됐을때 처리
api.interceptors.response.use(
	function (response: any) {
		return response;
	},
	async function (err: any) {
		if (err.response && err.response.status === 401) {
			const newAccessToken = err.response.headers.authorization;
			const newRefreshToken = err.response.headers.refresh;
			if (newAccessToken) {
				sessionStorage.setItem('login-token', JSON.stringify(newAccessToken));
				sessionStorage.setItem(
					'login-refresh',
					JSON.stringify(newRefreshToken)
				);
			}
			return Promise.reject(err);
		}
		return Promise.reject(err);
	}
);
export default api;
