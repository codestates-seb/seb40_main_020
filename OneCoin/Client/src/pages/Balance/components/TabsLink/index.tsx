import React from 'react';
import { NavLink } from 'react-router-dom';
import { TabList, TabItem } from './style';

interface TabsType {
	name: string;
	link: string;
}

const TabsLink = ({ array }: { array: TabsType[] }): React.ReactElement => {
	return (
		<TabList>
			{array.map((item: TabsType, key: number) => {
				const { name, link } = item;
				return (
					<TabItem key={`${name}-${key}`}>
						<NavLink
							to={link}
							className={({ isActive }) => (isActive ? 'active' : '')}
						>
							{name}
						</NavLink>
					</TabItem>
				);
			})}
		</TabList>
	);
};

export default TabsLink;
