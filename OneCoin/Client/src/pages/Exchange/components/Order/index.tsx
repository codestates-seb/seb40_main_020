import Button from 'components/Button';
import React, { useState, useEffect } from 'react';
import { OrderComponent } from './style';
import { CoinInfo } from '../../../../utills/types';

interface Props {
	inputPrice: number;
	setInputPrice: React.Dispatch<React.SetStateAction<number>>;
	coinInfo: CoinInfo;
}

function Order({ inputPrice, setInputPrice, coinInfo }: Props) {
	const money = 50000000;
	const coin = 2.64345;

	const [order, setOrder] = useState('매수');
	const menu = ['매수', '매도'];
	const menuClickHandler = (item: string) => setOrder(item);
	const [quantity, setQuantity] = useState<string>('0');
	const [total, setTotal] = useState<number>(0);
	const sizeBtnArr = [10, 25, 50, 75, 100];
	useEffect(() => {
		setQuantity('0');
		setTotal(0);
	}, [coinInfo.symbol]);
	useEffect(() => {
		setTotal(Math.round(inputPrice * +quantity));
	}, [inputPrice]);
	const quantityChangeHandler = (e: React.ChangeEvent<HTMLInputElement>) => {
		if (!Number.isNaN(+e.target.value)) {
			setQuantity(e.target.value);
			setTotal(Math.round(+e.target.value * inputPrice));
		}
	};
	const totalChangeHandler = (e: React.ChangeEvent<HTMLInputElement>) => {
		const value = e.target.value.replaceAll(',', '');
		if (!isNaN(+value)) setTotal(+value);
		setQuantity((+value / inputPrice).toFixed(8));
	};
	const priceChangeHandler = (e: React.ChangeEvent<HTMLInputElement>) => {
		const value = e.target.value.replaceAll(',', '');
		if (!isNaN(+value)) setInputPrice(+value);
		setTotal(Math.round(+value * +quantity));
	};
	const sizeBtnClickHandler = (size: number) => {
		if (order === '매수') {
			const sizePrice = (money / 100) * size;
			setTotal(sizePrice);
			setQuantity((sizePrice / inputPrice).toFixed(8));
		} else {
			const sizePrice = (coin / 100) * size;
			setQuantity(sizePrice + '');
			setTotal(Math.round(inputPrice * sizePrice));
		}
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
		console.log(Math.round(newInputPrice * +quantity).toLocaleString());
		setTotal(Math.round(newInputPrice * +quantity));
	};

	const tradeHandler = () => {
		console.log(
			`price = ${inputPrice}, quantity = ${quantity}, total=${total}`
		);
	};

	return (
		<OrderComponent className="order-wrapper">
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
					<div>
						{order === '매수'
							? `${money.toLocaleString()} KRW`
							: `${coin} ${coinInfo.symbol.replace('KRW', '')}`}
					</div>
				</div>
				<div className="order-price order">
					<div className="order-title">{`${order}가격`}</div>
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
							{sizeBtnArr.map((v, i) => (
								<div
									key={i}
									className="select-btn"
									onClick={() => sizeBtnClickHandler(v)}
								>
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
					<Button
						style={{ width: 455, height: 50, border: 'none' }}
						onClick={tradeHandler}
					>
						{order}
					</Button>
				</div>
			</div>
		</OrderComponent>
	);
}

export default Order;
