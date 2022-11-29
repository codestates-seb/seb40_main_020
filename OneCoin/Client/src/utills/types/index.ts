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

export interface CoinDataType {
	coin: string;
	symbol: string;
	code: string;
	ticker?: Ticker;
	orderBook?: OrderBook;
}

export interface Ticker {
	code: string;
	high_price: string;
	low_price: string;
	trade_price: string;
	prev_closing_price: string;
	change: string;
	change_price: string;
	change_rate: string;
	acc_trade_volume_24h: string;
	acc_trade_price_24h: string;
	timestamp: string;
}
export interface QuoteType {
	price: string;
	size: string;
	changeRate: string;
}

export interface AskOrder {
	askPrice: string;
	askSize: string;
	changeRate: string;
}
interface BidOrder {
	bidPrice: string;
	bidSize: string;
	changeRate: string;
}
export interface OrderBook {
	code: string;
	total_ask_size: string;
	total_bid_size: string;
	askInfo: AskOrder[];
	bidInfo: BidOrder[];
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
	open: string;
	high: string;
	low: string;
	close: string;
}

export interface ChatMsg {
	type: string;
	chatRoomId: number;
	message: string;
}

export interface ChatData {
	type: string;
	chatAt: string;
	chatRoomId: number;
	message: string;
	userDisplayName: string;
	userId: number;
}

export interface RoomsInfo {
	chatRoomId: number;
	numberOfChatters: number;
}
