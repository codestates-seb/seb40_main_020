import React, { useState, useEffect, useRef } from 'react';
import Quote from '../Quote';
import { QuoteListComponent } from './style';
import { OrderBook, AskOrder } from '../../../../utills/types';

interface Props {
	orderBook?: OrderBook;
	tradePrice?: string;
	prcieClickHandler: (price: number) => void;
}

function QuoteList({ orderBook, prcieClickHandler, tradePrice }: Props) {
	const scrollRef = useRef<null | HTMLDivElement>(null);
	const [ask, setAsk] = useState<AskOrder[]>([]);

	useEffect(() => {
		const askInfo = orderBook?.askInfo as AskOrder[];
		if (askInfo) {
			setAsk([...askInfo].reverse());
		}
	}, [orderBook]);
	return (
		<QuoteListComponent className="quote-wrapper" ref={scrollRef}>
			<div className="normal">일반호가</div>
			<div className="ask">
				{ask.map((ask, i) => (
					<Quote
						price={ask.askPrice}
						size={ask.askSize}
						changeRate={ask.changeRate}
						key={i}
						prcieClickHandler={prcieClickHandler}
						tradePrice={Number(tradePrice as string)}
					/>
				))}
			</div>
			<div className="bid">
				{orderBook?.bidInfo.map((bid, i) => (
					<Quote
						price={bid.bidPrice}
						size={bid.bidSize}
						changeRate={bid.changeRate}
						key={i}
						prcieClickHandler={prcieClickHandler}
						tradePrice={Number(tradePrice as string)}
					/>
				))}
			</div>
		</QuoteListComponent>
	);
}

export default QuoteList;
