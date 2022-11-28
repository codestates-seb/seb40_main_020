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

interface CoinBase {
	code?: string;
	amount?: string;
}

export interface CoinInfo {
	coin: string;
	code: string;
	symbol: string;
}

export interface MyCoins extends CoinBase {
	priceEvaluation?: string;
	averagePrice?: string;
	change?: string;
	changePrice?: string;
	changeRate?: string;
}
export interface CompleteOrders extends CoinBase {
	completedTime?: string;
	orderType?: string;
	price?: string;
}
export interface NonTradingOders extends CoinBase {
	orderTime?: string;
	orderType?: string;
	limit?: string;
	market?: string;
	stopLimit?: string;
}
interface Ticker {
	code: string;
	highPrice: number;
	lowPrice: number;
	tradePrice: number;
	prevClosingPrice: number;
	change: string;
	changePrice: number;
	changeRate: number;
	accTradeVolume24h: number;
	accTradePrice24h: number;
}
interface Order {
	askPrice: number;
	askSize: number;
	changeRate: string;
}
interface Orderbook {
	code: string;
	totalAskSize: number;
	totalBidSize: number;
	askInfo: Order[];
	bidInfo: Order[];
}
interface Trade {
	limit: number;
	market: string;
	stopLimit: number;
	amount: number;
	orderType: string;
}
interface HoldingCoin extends CoinBase {
	priceEvaluation: string;
	averagePrice: string;
	change: string;
	changePrice: string;
	changeRate: string;
}

export interface ChartData {
	time: number;
	open: number;
	high: number;
	low: number;
	close: number;
}

export interface ChatMsg {
	type: string;
	userDisplayName: string;
	userId: number;
	chatRoomId: number;
	message: string;
}

export interface ChatData {
	chatAt: string;
	chatRoomId: number;
	message: string;
	userDisplayName: string;
}
