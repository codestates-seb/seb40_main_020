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
	const rate = +changeRate.replace('%', '').replace('+', '');
	return (
		<QuoteComponent
			tradePrice={tradePrice}
			price={+price}
			onClick={() => prcieClickHandler(+price)}
		>
			<div
				className={rate > 0 ? 'price rise' : +rate < 0 ? 'price fall' : 'price'}
			>
				<div>{Number(price).toLocaleString()}</div>
				<div className="rate">{changeRate}</div>
			</div>
			<div
				className={rate > 0 ? 'size rise' : +rate < 0 ? 'size fall' : 'size'}
			>
				{Number(size).toFixed(4)}
			</div>
		</QuoteComponent>
	);
}

export default Quote;
