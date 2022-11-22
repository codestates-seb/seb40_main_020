import Tab from 'components/Tab';
import React from 'react';
import Hold from '../Hold';
import { HoldListComponent } from './style';

function HoldList() {
	const menu = ['보유코인', '미체결'];
	const el = [
		<Hold key={menu[0]} title={'보유 코인'} />,
		<Hold key={menu[1]} title={'미체결'} />,
	];
	return (
		<HoldListComponent className="hold-wrapper">
			<Tab menu={menu} el={el} />
		</HoldListComponent>
	);
}

export default HoldList;
