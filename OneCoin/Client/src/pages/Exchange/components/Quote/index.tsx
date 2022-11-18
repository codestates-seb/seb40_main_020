import React, { useEffect } from 'react';
import { QuoteComponent } from './style';
import { OrderType } from '../../../../utills/types';

interface Props extends OrderType {
	prcieClickHandler: (price: number) => void;
	tradePrice?: number;
}

function Quote({ price, size, prcieClickHandler, tradePrice }: Props) {
	return (
		<QuoteComponent
			tradePrice={tradePrice}
			price={price}
			onClick={() => prcieClickHandler(price)}
		>
			<div className="price">{price.toLocaleString()}</div>
			<div className="size">{size.toFixed(3)}</div>
		</QuoteComponent>
	);
}

export default Quote;
