import React, { memo } from 'react';
import { AsideComponent } from './style';
import Tab from 'components/Tab';
import CoinList from './CoinList';

export interface Props {
	symbolHandler: (item: { coin: string; code: string; symbol: string }) => void;
}

function Aside({ symbolHandler }: Props) {
	const menu = ['코인', '랭킹'];
	const el = [<CoinList symbolHandler={symbolHandler} key={menu[0]} />];
	return (
		<AsideComponent>
			<Tab menu={menu} el={el} />
		</AsideComponent>
	);
}

export default memo(Aside);
