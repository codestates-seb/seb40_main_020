import React, { useEffect } from 'react';
import { QuoteComponent } from './style';
import { QuoteType } from '../../../../utills/types';

interface Props extends QuoteType {
	prcieClickHandler: (price: number) => void;
	tradePrice?: number;
}

function Quote({
	price,
	size,
	prcieClickHandler,
	tradePrice,
	changeRate,
}: Props) {
	return (
		<QuoteComponent
			tradePrice={tradePrice}
			price={+price}
			onClick={() => prcieClickHandler(+price)}
		>
			<div className="price">
				<div>{Number(price).toLocaleString()}</div>
				<div className="rate today-range">{changeRate}</div>
			</div>
			<div className="size">{Number(size).toFixed(2)}</div>
		</QuoteComponent>
	);
}

export default Quote;
