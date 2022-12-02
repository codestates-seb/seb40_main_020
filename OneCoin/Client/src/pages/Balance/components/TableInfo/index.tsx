import React, { useEffect, useState } from 'react';
import { BALANCE_INFO_LIST } from 'utills/constants/investments';
import { Wrapper, MenuList, MenuItem } from './style';
import { useRecoilValue, useRecoilState } from 'recoil';
import { myCoinsState, myBalanceState, isLogin } from '../../../../store';
import { getBalance } from '../../../../api/balance';
import { rateCalculator } from '../../../../utills/calculate';
const TabInfo = () => {
	const login = useRecoilValue(isLogin);
	const [data, setData] = useState<number[]>([]);
	const myCoins = useRecoilValue(myCoinsState);
	const [myBalance, setMyBalance] = useRecoilState(myBalanceState);

	const getBalanceHandler = async () => {
		try {
			const res = await getBalance();
			setMyBalance(res);
		} catch (err) {
			console.log(err);
		}
	};
	useEffect(() => {
		if (login) getBalanceHandler();
	}, []);
	const rpa = (s: string) =>
		myCoins.reduce((a, b) => a + +(b[s] as string).replaceAll(',', ''), 0);
	useEffect(() => {
		if (myCoins !== undefined && (myCoins[0]?.totalPrice as string) && login) {
			const buyTotal = rpa('totalPrice');
			const evalutionTotal = rpa('priceEvaluation');
			const proceeds = rpa('changePrice');
			const newData = [
				myBalance,
				myBalance + evalutionTotal,
				buyTotal,
				proceeds,
				evalutionTotal,
				rateCalculator(evalutionTotal, buyTotal),
			];
			setData(newData);
		}
	}, [myCoins]);
	return (
		<Wrapper>
			{BALANCE_INFO_LIST.map((balance, index) => {
				const { name, currency } = balance;
				return (
					<MenuList key={`${name}-${index}`} proceeds={data[3] as number}>
						<MenuItem>{name}</MenuItem>
						<MenuItem>{data[index] && data[index].toLocaleString()}</MenuItem>
						<MenuItem>{currency}</MenuItem>
					</MenuList>
				);
			})}
		</Wrapper>
	);
};

export default TabInfo;
