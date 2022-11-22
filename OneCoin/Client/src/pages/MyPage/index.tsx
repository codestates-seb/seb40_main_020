import React from 'react';
import Layout from 'components/Layout';

import { Wrapper } from './style';

const MyPage = () => {
	return (
		<Layout isLeftSidebar={false}>
			<Wrapper>
				<ul>
					<li>회원정보 수정</li>
					<li>회원탈퇴</li>
				</ul>
				<div>
					<div>
						<img src="" alt="profile 이미지" />
						<ul>
							<li>닉네임: ???</li>
							<li>이메일: 1234@gmail.com</li>
						</ul>
					</div>
				</div>
			</Wrapper>
		</Layout>
	);
};

export default MyPage;
