import React from 'react';
import { CoinListComponent } from './style';

function Rank() {
	return (
		<CoinListComponent>
			<table className="sub-title">
				<thead>
					<tr>
						<td>순위</td>
						<td>닉네임</td>
						<td>수익률</td>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>1</td>
						<td>닉네임</td>
						<td className={'rise'}>
							<span>1%</span>
						</td>
					</tr>
				</tbody>
			</table>
		</CoinListComponent>
	);
}

export default Rank;
