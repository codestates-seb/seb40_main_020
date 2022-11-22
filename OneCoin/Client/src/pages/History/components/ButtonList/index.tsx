import React, { useState } from 'react';
import { Wrapper } from './style';

interface Props {
	title: string;
	value: string;
}

const ButtonList = ({ title, list }: { title?: string; list: Props[] }) => {
	const [btnActive, setBtnActive] = useState('');

	const handleValue: React.MouseEventHandler<HTMLElement> = (e) => {
		setBtnActive(e.currentTarget.dataset['value'] ?? '');
	};

	return (
		<Wrapper>
			<span>{title && title}</span>
			<ul>
				{list.map((item, i) => {
					return (
						<li key={`${item.title}-${i}`}>
							<button
								className={item.value === btnActive ? 'active' : ''}
								onClick={handleValue}
								data-value={item.value}
							>
								{item.title}
							</button>
						</li>
					);
				})}
			</ul>
		</Wrapper>
	);
};

export default ButtonList;
