import React from 'react';
import { AsideComponent } from './style';

function Rank() {
	return (
		<AsideComponent>
			<table className="sub-title">
				<thead>
					<tr>
						<td>순위</td>
						<td>닉네임</td>
						<td>이메일</td>
						<td>수익률</td>
					</tr>
				</thead>
				<tbody>
					{
						<tr>
							<td>1</td>
							<td>닉네임</td>
							<td>이메일</td>
							<td className={'today-range'}>
								<span>1</span>
							</td>
						</tr>
					}
				</tbody>
			</table>
		</AsideComponent>
	);
}

export default Rank;
