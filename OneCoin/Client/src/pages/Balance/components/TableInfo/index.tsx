import React from 'react';
import { BALANCE_INFO_LIST } from 'utills/constants/investments';
import { Wrapper, MenuList, MenuItem } from './style';

const TabInfo = () => {
	return (
		<Wrapper>
			{BALANCE_INFO_LIST.map((balance, index) => {
				const { name, currency } = balance;
				return (
					<MenuList key={`${name}-${index}`}>
						<MenuItem>{name}</MenuItem>
						<MenuItem>0</MenuItem>
						<MenuItem>{currency}</MenuItem>
					</MenuList>
				);
			})}
		</Wrapper>
	);
};

export default TabInfo;
