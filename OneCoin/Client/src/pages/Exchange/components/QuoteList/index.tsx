import React, { useEffect, useState } from 'react';
import Quote from '../Quote';
import { QuoteListComponent } from './style';
import { OrderbookType, OrderType } from '../../../../utills/types';

interface Props {
	coinOrderbook?: OrderbookType;
	tradePrice?: number;
	prcieClickHandler: (price: number) => void;
}

function QuoteList({ coinOrderbook, prcieClickHandler, tradePrice }: Props) {
	const [askPrice, setAskPrice] = useState<OrderType[]>([]);
	const [bidPrice, setBidPrice] = useState<OrderType[]>([]);
	useEffect(() => {
		if (coinOrderbook?.orderbook_units !== undefined) {
			const newAskPrice = coinOrderbook.orderbook_units.map((v) => {
				const obj = { price: v.ask_price, size: v.ask_size };
				return obj;
			});
			setAskPrice(newAskPrice.sort((a, b) => b.price - a.price));
			const newBidPrice = coinOrderbook.orderbook_units.map((v) => {
				const obj = { price: v.bid_price, size: v.bid_size };
				return obj;
			});
			setBidPrice(newBidPrice);
		}
	}, [coinOrderbook]);

	return (
		<QuoteListComponent>
			<div className="normal">일반호가</div>
			<div className="ask">
				{askPrice.map((ask, i) => (
					<Quote
						price={ask.price}
						size={ask.size}
						key={i}
						prcieClickHandler={prcieClickHandler}
						tradePrice={tradePrice}
					/>
				))}
			</div>
			<div className="bid">
				{bidPrice.map((bid, i) => (
					<Quote
						price={bid.price}
						size={bid.size}
						key={i}
						prcieClickHandler={prcieClickHandler}
						tradePrice={tradePrice}
					/>
				))}
			</div>
		</QuoteListComponent>
	);
}

export default QuoteList;
