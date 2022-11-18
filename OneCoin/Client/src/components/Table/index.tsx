import React from 'react';
import { Wrapper, Header, Body } from './style';

interface bodyTypes {
	id: number;
	[key: string]: string | number;
}
interface TableProps {
	title?: string;
	headerGroups: string[];
	bodyDatas: bodyTypes[];
}

const Table = ({ title, headerGroups, bodyDatas }: TableProps) => {
	return (
		<Wrapper>
			{title && <caption>{title}</caption>}
			<Header>
				<tr>
					{headerGroups.map((header, index) => {
						return <th key={`${header}-${index}`}>{header}</th>;
					})}
				</tr>
			</Header>
			<Body>
				{bodyDatas.map((data, i) => {
					const keys = Object.keys(data).filter((key) => key !== 'id');

					return (
						<tr key={i}>
							{keys.map((key, i) => (
								<td key={i}>{data[key]}</td>
							))}
						</tr>
					);
				})}
			</Body>
		</Wrapper>
	);
};

export default Table;
