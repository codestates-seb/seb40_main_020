import React from 'react';
import Layout from 'components/Layout';
import Lottie from 'react-lottie-player';
import styled from 'styled-components';
import errorJson from '../assets/images/404.json';
import { flexColumnCenter } from 'styles';

const Wrapper = styled.div`
	${flexColumnCenter}
	margin-top:5rem;
	p {
		color: #333;
	}
`;

const Error = () => {
	return (
		<Layout isLeftSidebar={false}>
			<Wrapper>
				<Lottie loop animationData={errorJson} play style={{ width: 400 }} />
				<p>존재하지 않는 페이지 입니다.</p>
			</Wrapper>
		</Layout>
	);
};

export default Error;
