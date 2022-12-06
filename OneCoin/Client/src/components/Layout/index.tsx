import React, { useState } from 'react';
import { Wapper, Container } from './style';
import Aside from '../Aside/index';
import Header from 'components/Header';
import Footer from 'components/Footer';

type Props = {
	children: React.ReactNode;
	isLeftSidebar?: boolean;
	isLeftMargin?: boolean;
};
interface T {
	coin: string;
	code: string;
	symbol: string;
}

function Layout({
	children,
	isLeftSidebar = true,
	isLeftMargin = true,
}: Props) {
	const [symbol, setSymbol] = useState<T>({
		coin: '비트코인',
		code: 'KRW-BTC',
		symbol: 'BTCKRW',
	});
	const coinInfoHandler = (item: T) => setSymbol(item);

	return (
		<>
			<Header />
			<Wapper isLeftMargin={isLeftMargin}>
				<Container>{children}</Container>
				<Aside
					coinInfoHandler={coinInfoHandler}
					isLeftSidebar={isLeftSidebar}
				/>
			</Wapper>
			<Footer />
		</>
	);
}

export default Layout;
