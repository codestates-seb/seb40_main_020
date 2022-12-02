import Button from 'components/Button';
import React, { useState, useEffect } from 'react';
import { OrderComponent } from './style';
import { CoinInfo } from '../../../../utills/types';
import { postOrder, getNonTrading } from '../../../../api/exchange';
import { getBalance } from '../../../../api/balance';
import {
	nonTradingOdersState,
	myCoinsState,
	coinInfoState,
	isLogin,
} from '../../../../store';
import { useRecoilValue, useRecoilState } from 'recoil';

interface Props {
	inputPrice: number;
	setInputPrice: React.Dispatch<React.SetStateAction<number>>;
}

function Order({ inputPrice, setInputPrice }: Props) {
	const login = useRecoilValue(isLogin);
	const [money, setMoney] = useState(0);
	const [coin, setCoin] = useState(0);
	const [nonTradingOrders, setNonTradingOrders] =
		useRecoilState(nonTradingOdersState);
	const myCoins = useRecoilValue(myCoinsState);
	const coinInfo = useRecoilValue(coinInfoState);

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
	useEffect(() => {
		console.log('실행');
		if (login) getMyBalance();
	}, [order]);
	const getMyBalance = async () => {
		try {
			const res = await getBalance();
			setMoney(res);
		} catch (err) {
			console.log(err);
		}
	};

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
			const commission = sizePrice * 0.0005;
			setTotal(sizePrice - commission);
			setQuantity(((sizePrice - commission) / inputPrice).toFixed(8));
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
		setTotal(Math.round(newInputPrice * +quantity));
	};
	const getNonTradingData = async () => {
		try {
			const res = await getNonTrading();
			setNonTradingOrders([...res]);
		} catch (err) {
			console.log(err);
		}
	};

	useEffect(() => {
		const m = myCoins !== undefined && myCoins.length;
		const n = nonTradingOrders !== undefined && nonTradingOrders.length;
		if (m && n && isLogin) {
			const c = myCoins.filter((v) => v.code === coinInfo.code)[0].amount;
			const non = nonTradingOrders
				.filter((v) => v.code === coinInfo.code)
				.reduce((a, b) => {
					if (b.orderType === 'ASK') return a + Number(b.amount);
					else return a;
				}, 0);
			setCoin(+c - non);
		} else if (m && isLogin) {
			const c = myCoins.filter((v) => v.code === coinInfo.code)[0].amount;
			setCoin(+c);
		}
	}, [nonTradingOrders, myCoins, order]);
	const tradeHandler = () => {
		if (inputPrice === 0) return alert('가격을 확인해 주세요');
		else if (login) {
			const t = async () => {
				try {
					await postOrder(coinInfo.code, data);
					await getNonTradingData();
					await getMyBalance();
				} catch (err) {
					console.log(err);
				}
			};
			const data = {
				limit: inputPrice + '',
				market: '0',
				stopLimit: '0',
				amount: quantity,
				orderType: 'BID',
			};

			if (order === '매수') {
				if (total * 1.0005 > money) {
					alert('주문수량을 확인해 주세요');
				} else {
					data.orderType = 'BID';
					t();
				}
			} else if (order === '매도') {
				if (coin < +quantity) alert('주문수량을 확인해 주세요');
				else {
					data.orderType = 'ASK';
					t();
				}
			}
		} else {
			alert('로그인이 필요한 서비스입니다.');
		}
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
							? `${money ? money.toLocaleString() : 0} KRW`
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
