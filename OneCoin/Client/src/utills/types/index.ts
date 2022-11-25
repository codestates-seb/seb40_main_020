export interface TickerType {
	acc_ask_volume?: number;
	acc_bid_volume?: number;
	acc_trade_price?: number;
	acc_trade_price_24h?: number;
	acc_trade_volume?: number;
	acc_trade_volume_24h?: number;
	ask_bid?: string;
	change?: string;
	change_price?: number;
	change_rate?: number;
	code?: string;
	delisting_date?: null;
	high_price?: number;
	highest_52_week_date?: string;
	highest_52_week_price?: number;
	is_trading_suspended?: boolean;
	low_price?: number;
	lowest_52_week_date?: string;
	lowest_52_week_price?: number;
	market_state?: string;
	market_warning?: string;
	opening_price?: number;
	prev_closing_price?: number;
	signed_change_price?: number;
	signed_change_rate?: number;
	stream_type?: string;
	timestamp?: number;
	trade_date?: string;
	trade_price?: number;
	trade_time?: string;
	trade_timestamp?: number;
	trade_volume?: number;
	type?: string;
}

export interface OrderbookType {
	code?: string;
	orderbook_units?: {
		ask_price: number;
		bid_price: number;
		ask_size: number;
		bid_size: number;
	}[];
	stream_type?: string;
	timestamp?: number;
	total_ask_size?: number;
	total_bid_size?: number;
	type?: string;
}

export interface CoinDataType {
	coin: string;
	symbol: string;
	code: string;
	ticker?: TickerType;
	orderbook?: OrderbookType;
}

export interface coinAsiderData {
	coin: string;
	symbol: string;
	code: string;
	tradePrice?: number;
	change?: string;
	signedChangePrice?: number;
	signedChangeRate?: number;
}
// enum
export interface OrderType {
	price: number;
	size: number;
}
export interface MyCoins {
	code?: string;
	amount?: string;
	priceEvaluation?: string;
	averagePrice?: string;
	change?: string;
	changePrice?: string;
	changeRate?: string;
}
export interface CompleteOrders {
	code?: string;
	completedTime?: string;
	orderType?: string;
	price?: string;
	amount?: string;
}
export interface NonTradingOders {
	code?: string;
	orderTime?: string;
	orderType?: string;
	limit?: string;
	market?: string;
	stopLimit?: string;
	amount?: string;
}
