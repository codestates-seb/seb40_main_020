import React from 'react';
import Layout from 'components/Layout';

import { Wrapper } from './style';

const MyPage = () => {
	return (
		<Layout isLeftSidebar={false}>
			<Wrapper>
				<ul className="side-menu">
					<li>회원정보 수정</li>
					<li>회원탈퇴</li>
				</ul>
				<div className="content-wrapper">
					<div className="profile-box">
						<img src="" alt="profile 이미지" />
						<ul>
							<li>닉네임: ???</li>
							<li>이메일: 1234@gmail.com</li>
						</ul>
					</div>
					<div className="input-box">
						<span>닉네임 변경</span>
						<input type="text" placeholder="닉네임" />
					</div>
					<div className="input-box">
						<span>비밀번호 변경</span>
						<input type="password" placeholder="비밀번호" />
					</div>
					<button>수정하기</button>
				</div>
			</Wrapper>
		</Layout>
	);
};

export default MyPage;
