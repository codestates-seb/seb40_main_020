import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { CoinListComponent } from './style';

type user = {
	rank: number;
	displayName: string;
	ROI: string;
	roi: string;
};

function Rank() {
	const [users, setUsers] = useState<user[]>([]);
	const [loading, setLoading] = useState(false);

	useEffect(() => {
		axios
			.get(`${process.env.REACT_APP_SERVER_URL}/api/ranks`)
			.then((response) => {
				setUsers(response.data.users);
				setLoading(true);
			});
	}, []);

	return (
		<>
			{loading && (
				<CoinListComponent>
					<table className="sub-title">
						<thead>
							<tr>
								<td>순위</td>
								<td>닉네임</td>
								<td>수익률</td>
							</tr>
						</thead>
						{users &&
							users.map((user, idx) => {
								return (
									<tbody key={idx}>
										<tr>
											<td>{user.rank}</td>
											<td>{user.displayName}</td>
											<td className={user.ROI[0] === '-' ? 'fall' : 'rise'}>
												<span>{user.ROI}</span>
											</td>
										</tr>
									</tbody>
								);
							})}
					</table>
				</CoinListComponent>
			)}
		</>
	);
}

export default Rank;
