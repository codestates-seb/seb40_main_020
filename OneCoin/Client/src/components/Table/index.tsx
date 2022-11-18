import React from 'react';
import { Wrapper, Header, Body } from './style';

interface bodyTypes {
	id: number;
	coin: string;
	quantity: string; // 보유수량
	purchase_avg_price: string; // 매수평균가
	purchase_amount: string; // 매수금액
	appraisal_amount: string; // 평가금액
	valuation_gains_losses: string; // 평가손익
}
interface TableProps {
	title: string;
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
				{bodyDatas.map((data) => {
					const {
						id,
						coin,
						quantity,
						purchase_avg_price,
						purchase_amount,
						appraisal_amount,
						valuation_gains_losses,
					} = data;
					return (
						<tr key={`${coin}-${id}`}>
							<td>{coin}</td>
							<td>{quantity}</td>
							<td>{purchase_avg_price}</td>
							<td>{purchase_amount}</td>
							<td>{appraisal_amount}</td>
							<td>{valuation_gains_losses}</td>
						</tr>
					);
				})}
			</Body>
		</Wrapper>
	);
};

export default Table;
