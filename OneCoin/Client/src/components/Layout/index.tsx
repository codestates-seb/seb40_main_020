import React from 'react';
import { Wapper, Container, Aside } from './style';

type Props = {
	children: React.ReactNode;
};
function Layout({ children }: Props) {
	return (
		<Wapper>
			<Container>{children}</Container>
			<Aside>사이드바</Aside>
		</Wapper>
	);
}

export default Layout;
