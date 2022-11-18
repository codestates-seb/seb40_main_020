import React, { useState } from 'react';
import { Wapper, Container } from './style';
import Aside from '../Aside/index';

type Props = {
	children: React.ReactNode;
	isLeftSidebar?: boolean;
};
interface T {
	coin: string;
	code: string;
	symbol: string;
}

function Layout({ children, isLeftSidebar = true }: Props) {
	const [symbol, setSymbol] = useState<T>({
		coin: '비트코인',
		code: 'KRW-BTC',
		symbol: 'BTCKRW',
	});
	const symbolHandler = (item: T) => setSymbol(item);

	return (
		<Wapper>
			<Container>{children}</Container>
			<Aside symbolHandler={symbolHandler} isLeftSidebar={isLeftSidebar} />
		</Wapper>
	);
}

export default Layout;
