import Button from 'components/Button';
import React, { useState } from 'react';
import { OrderComponent } from './style';

interface Props {
	inputPrice: number | string;
	priceChangeHandler: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

function Order({ inputPrice, priceChangeHandler }: Props) {
	const [order, setOrder] = useState('매수');
	const menu = ['매수', '매도'];
	const menuClickHandler = (item: string) => setOrder(item);
	const money = 50000000;
	const [quantity, setQuantity] = useState<number | string>(0);
	const [total, setTotal] = useState<number | string>(0);
	const quantityChangeHandler = (e: React.ChangeEvent<HTMLInputElement>) => {
		setQuantity(e.target.value);
		setTotal(+e.target.value * +inputPrice);
	};
	const totalChangeHandler = (e: React.ChangeEvent<HTMLInputElement>) => {
		setQuantity((+e.target.value / +inputPrice).toFixed(8));
		setTotal(e.target.value);
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
							onChange={(e) => priceChangeHandler(e)}
						/>
						<button>+</button>
						<button>-</button>
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
							{[10, 25, 50, 75, '직접입력'].map((v, i) => (
								<div key={i} className="select-btn">
									{v !== '직접입력' ? `${v}%` : v}
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
						로그인
					</Button>
				</div>
			</div>
		</OrderComponent>
	);
}

export default Order;
