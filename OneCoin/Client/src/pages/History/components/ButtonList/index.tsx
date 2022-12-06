import React, { useState } from 'react';
import { Wrapper } from './style';

interface Props {
	title: string;
	value: string;
}
type F = (s: string) => void;
const ButtonList = ({
	title,
	list,
	btnHandler,
}: {
	title?: string;
	list: Props[];
	btnHandler: F;
}) => {
	const [btnActive, setBtnActive] = useState('');
	const handleValue: React.MouseEventHandler<HTMLElement> = (e) => {
		btnHandler(e.currentTarget.dataset['value'] as string);
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
