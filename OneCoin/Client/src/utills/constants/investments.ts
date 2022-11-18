export const INVERSTMENTS_LIST = [
	{ name: '보유자산', link: '/investments/balance' },
	{ name: '거래내역', link: '/investments/history' },
	{ name: '미체결', link: '/investments/wait_orders' },
];

export const BALANCE_INFO_LIST = [
	{ name: '보유 KRW', currency: 'KRW' },
	{ name: '총 보유자산', currency: 'KRW' },
	{ name: '총 매수금액', currency: 'KRW' },
	{ name: '총 평가손익', currency: 'KRW' },
	{ name: '총 평가금액', currency: 'KRW' },
	{ name: '총 평가수익률', currency: '%' },
];

export const BALANCE_THEAD = [
	'보유자산',
	'보유수량',
	'매수평균가',
	'매수금액',
	'평가금액',
	'평가손익(%)',
];

export const BALANCE_TBODY = {
	data: [
		{
			id: 1,
			coin: '비트토렌트', // 보유자산
			quantity: '0.00000417 BTT', // 보유수량
			purchase_avg_price: '0.0013 KRW', // 매수평균가
			purchase_amount: '1 KRW', // 매수금액
			appraisal_amount: '0 KRW', // 평가금액
			valuation_gains_losses: '-15.38%', // 평가손익
		},
	],
	pageInfo: {
		page: 1,
		size: 10,
		totalElements: 100,
		totalPages: 10,
	},
};
