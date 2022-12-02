import React, { useState } from 'react';
import { HISTORY_PERIOD, HISTORY_TYPE } from 'utills/constants/investments';
import ButtonList from '../ButtonList';
import { Wrapper, SearchBox } from './style';

import { AiOutlineSearch } from 'react-icons/ai';
type F = (s: string) => void;
interface T {
	periodHandler: F;
	typeHandler: F;
	getCompleteTradeData: (code: string) => void;
}
const HistoryHeader = ({
	periodHandler,
	typeHandler,
	getCompleteTradeData,
}: T) => {
	const [code, setCode] = useState('');
	const codeHandler = (e: React.ChangeEvent<HTMLInputElement>) => {
		setCode(e.target.value);
	};
	return (
		<Wrapper>
			<ButtonList
				title="기간"
				list={HISTORY_PERIOD}
				btnHandler={periodHandler}
			/>
			<ButtonList title="종류" list={HISTORY_TYPE} btnHandler={typeHandler} />
			<SearchBox>
				<span>코인선택</span>
				<div>
					<AiOutlineSearch
						className="icon"
						onClick={() => getCompleteTradeData(code)}
					/>
					<input type="text" placeholder="전체" onChange={codeHandler} />
				</div>
			</SearchBox>
		</Wrapper>
	);
};

export default HistoryHeader;
