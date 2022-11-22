// 입출금
export const BALANCES_THEAD = ['코인명', '보유비중', '보유수량'];

export const BALANCES_TBODY = {
	data: [
		{
			id: 1,
			coin: '비트토렌트', // 보유자산
			ratio: '0.00003%', // 보유비중
			quantity: '0.00000417 BTT', // 보유수량
		},
	],
	pageInfo: {
		page: 1,
		size: 10,
		totalElements: 100,
		totalPages: 10,
	},
};

export const DEPOSIT_THEAD = ['금액', '기타'];

export const DEPOSIT_TBODY = {
	data: [
		{
			id: 1,
			price: '비트토렌트', // 금액
			balance: '?????', // 기타
		},
	],
	pageInfo: {
		page: 1,
		size: 10,
		totalElements: 100,
		totalPages: 10,
	},
};
