import React, { memo } from 'react';
import { AsideComponent } from './style';
import Tab from 'components/Tab';
import CoinList from './CoinList';
import Rank from './Rank';
import { CoinInfo } from '../../utills/types';

export interface Props {
	coinInfoHandler: (item: CoinInfo) => void;
	isLeftSidebar?: boolean;
}

function Aside({ coinInfoHandler, isLeftSidebar }: Props) {
	const menu = ['코인', '랭킹'];
	const el = [
		<CoinList
			coinInfoHandler={coinInfoHandler}
			key={menu[0]}
			isLeftSidebar={isLeftSidebar}
		/>,
		<Rank key={menu[1]} />,
	];

	return (
		<AsideComponent isShow={isLeftSidebar}>
			<Tab menu={menu} el={el} />
		</AsideComponent>
	);
}

export default memo(Aside);
