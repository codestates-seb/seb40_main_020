import React from 'react';
import { AiOutlineSearch } from 'react-icons/ai';
import Table from 'components/Table';
import { BALANCES_THEAD, BALANCES_TBODY } from 'utills/constants/balances';

import { Wrapper } from './style';

const TotalAsset = () => {
	return (
		<Wrapper>
			<div className="title">
				총 보유자산
				<span>
					<strong>1</strong>KRW
				</span>
			</div>
			<div className="search-box">
				<AiOutlineSearch className="icon" />
				<input type="text" placeholder="코인명/심볼검색" />
			</div>
			<Table headerGroups={BALANCES_THEAD} bodyDatas={BALANCES_TBODY.data} />
		</Wrapper>
	);
};

export default TotalAsset;
