import React, { useState } from 'react';
import { TabComponent } from './style';

interface Props {
	menu: string[];
	el: React.ReactNode[];
}
function Tab({ menu, el }: Props) {
	const [title, setTitle] = useState(menu[0]);
	const [idx, setIdx] = useState(0);
	const titleClickHandler = (item: string) => setTitle(item);
	return (
		<TabComponent>
			<div className="title-menu">
				{menu.map((v, i) => (
					<div
						key={i}
						className={v === title ? 'select' : ''}
						onClick={() => {
							titleClickHandler(v);
							setIdx(i);
						}}
					>
						{v}
					</div>
				))}
			</div>
			<div>{el[idx]}</div>
		</TabComponent>
	);
}

export default Tab;
