import Button from 'components/Button';
import React, { useState } from 'react';
import { OrderComponent } from './style';

interface Props {
	inputPrice: number;
	setInputPrice: React.Dispatch<React.SetStateAction<number>>;
}

function Order({ inputPrice, setInputPrice }: Props) {
	const [order, setOrder] = useState('매수');
	const menu = ['매수', '매도'];
	const menuClickHandler = (item: string) => setOrder(item);
	const money = 50000000;
	const [quantity, setQuantity] = useState<number | string>(0);
	const [total, setTotal] = useState<number | string>(0);
	const quantityChangeHandler = (e: React.ChangeEvent<HTMLInputElement>) => {
		setQuantity(e.target.value);
		setTotal(+e.target.value * inputPrice);
	};
	const totalChangeHandler = (e: React.ChangeEvent<HTMLInputElement>) => {
		const value = e.target.value.replaceAll(',', '');
		if (!isNaN(+value)) setTotal(+value);
		setQuantity((+value / inputPrice).toFixed(8));
	};
	const priceChangeHandler = (e: React.ChangeEvent<HTMLInputElement>) => {
		const value = e.target.value.replaceAll(',', '');
		if (!isNaN(+value)) setInputPrice(+value);
		setTotal(+value * +quantity);
	};
	const sizeBtnClickHandler = (size: number) => {
		console.log((money / 100) * size);
		const sizePrice = (money / 100) * size;
		setTotal(sizePrice);
		setQuantity((sizePrice / inputPrice).toFixed(8));
	};
	const btnHandler = (sign: string) => {
		let additionalValue: number;
		if (inputPrice < 100) additionalValue = 0.1;
		else if (inputPrice < 1000) additionalValue = 1;
		else if (inputPrice < 10000) additionalValue = 5;
		else if (inputPrice < 100000) additionalValue = 10;
		else if (inputPrice < 1000000) additionalValue = 50;
		else if (inputPrice < 10000000) additionalValue = 500;
		else additionalValue = 1000;
		const newInputPrice =
			sign === '+'
				? inputPrice + additionalValue
				: inputPrice - additionalValue;
		setInputPrice(newInputPrice);
	};
	return (
		<OrderComponent>
			<div className="order-menu">
				{menu.map((v, i) => (
					<div
						key={i}
						className={order === v ? 'menu-item select' : 'menu-item'}
						onClick={() => menuClickHandler(v)}
					>
						{v}
					</div>
				))}
			</div>
			<div className="order-contents">
				<div className="holding-money order">
					<div className="order-title">주문가능</div>
					<div>{`${money.toLocaleString()} KRW`}</div>
				</div>
				<div className="order-price order">
					<div className="order-title">매수가격</div>
					<div className="order-input">
						<input
							type="text"
							value={inputPrice.toLocaleString()}
							onChange={priceChangeHandler}
						/>
						<button onClick={() => btnHandler('+')}>+</button>
						<button onClick={() => btnHandler('-')}>-</button>
					</div>
				</div>
				<div className="order-size order">
					<div className="order-title">주문수량</div>
					<div>
						<div className="order-input">
							<input
								type="text"
								value={quantity}
								onChange={quantityChangeHandler}
							/>
						</div>
						<div className="size-btn-wrapper">
							{[10, 25, 50, 75, 100].map((v, i) => (
								<div
									key={i}
									className="select-btn"
									onClick={() => sizeBtnClickHandler(v)}
								>
									{/* {v !== '직접입력' ? `${v}%` : v} */}
									{v + '%'}
								</div>
							))}
						</div>
					</div>
				</div>
				<div className="total-price order">
					<div className="order-title">주문총액</div>
					<div className="order-input">
						<input
							type="text"
							value={total.toLocaleString()}
							onChange={totalChangeHandler}
						/>
					</div>
				</div>
				<div className="order-btn">
					<Button style={{ width: 455, height: 50, border: 'none' }}>
						매수
					</Button>
				</div>
			</div>
		</OrderComponent>
	);
}

export default Order;
