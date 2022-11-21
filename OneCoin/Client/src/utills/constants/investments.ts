// balance 보유자산
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

export const HEADER_LIST = {
	page: [
		{ name: '거래소', path: '/exchange' },
		{ name: '입금', path: '/balancese' },
		{ name: '투자내역', path: '/investments/balance' },
		{ name: '스왑', path: '/swap' },
	],
	logout: [
		{ name: '로그인', path: '/login' },
		{ name: '회원가입', path: '/signup' },
	],
	login: [{ name: '마이페이지', path: '/mypage' }],
};

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

// history 거래내역
export const HISTORY_THEAD = [
	'체결시간',
	'코인',
	'마켓',
	'종류',
	'거래수량',
	'거래단가',
	'거래금액',
	'수수료',
	'정산금액',
	'주문시간',
];

export const HISTORY_TBODY = {
	data: [
		{
			id: 1,
			execution_time: '2022. 09.09. 07:58', // 체결시간
			coin: '비트토렌트', // 코인
			market: '-', // 마켓
			type: '입금', //종류
			quantity: '0.00000417 BTT', // 거래수량
			unit_price: '0 KRW', // 거래단가
			transaction_amount: '0 KRW', // 거래금액
			premium: '0 APENFT', // 수수료
			settlement_amount: '0.0000000001 APENFT', // 정산금액
			order_time: '-', // 주문시간
		},
	],
	pageInfo: {
		page: 1,
		size: 10,
		totalElements: 100,
		totalPages: 10,
	},
};

export const HISTORY_PERIOD = [
	{ title: '1주일', value: '' },
	{ title: '1개월', value: '1' },
	{ title: '3개월', value: '3' },
	{ title: '6개월', value: '6' },
];

export const HISTORY_TYPE = [
	{ title: '전체', value: '' },
	{ title: '매수', value: 'buy' },
	{ title: '매도', value: 'sell' },
	{ title: '입금', value: 'deposit' },
	{ title: '스왑', value: 'swap' },
];

// wait_orders 미체결
export const WAIT_ORDERS_THEAD = [
	'시간',
	'마켓명',
	'거래종류',
	'감시가격',
	'주문가격',
	'주문수량',
	'미체결량',
	'주문취소',
];

export const WAIT_ORDERS_TBODY = {
	data: [
		{
			id: 1,
			execution_time: '2022. 09.09. 07:58', // 시간
			market: '-', // 마켓명
			type: '입금', //거래종류
			watch_price: '0.00000417 BTT', // 감시가격
			order_price: '0.00000417 BTT', // 주문가격
			order_quantity: '0.00000417 BTT', // 주문수량
			not_signed: '0.00000417 BTT', // 미체결량
			order_cancel: '0.00000417 BTT', // 주문취소
		},
	],
	pageInfo: {
		page: 1,
		size: 10,
		totalElements: 100,
		totalPages: 10,
	},
};
