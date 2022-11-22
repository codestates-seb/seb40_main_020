import React from 'react';
import { AsideComponent } from './style';
import Tab from 'components/Tab';
import CoinList from './CoinList';

export interface Props {
	symbolHandler: (item: { coin: string; code: string; symbol: string }) => void;
	isLeftSidebar?: boolean;
}

function Aside({ symbolHandler, isLeftSidebar }: Props) {
	const menu = ['코인', '랭킹'];
	const el = [<CoinList symbolHandler={symbolHandler} key={menu[0]} />];
	return (
		<AsideComponent isShow={isLeftSidebar}>
			<Tab menu={menu} el={el} />
		</AsideComponent>
	);
}

export default Aside;
