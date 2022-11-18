import Layout from 'components/Layout';
import React from 'react';
import { Link } from 'react-router-dom';
import Lottie from 'react-lottie-player';
import Slider from 'react-slick';
import exchangeJson from '../../assets/images/exchange.json';
import chatJson from '../../assets/images/chat.json';

import { Wrap, SlideWrap, SlideInner, TextWrap } from './style';
import Button from 'components/Button';

const Main = () => {
	const settings = {
		dots: true,
		infinite: true,
		speed: 500,
		slidesToShow: 1,
		slidesToScroll: 1,
		arrows: false,
	};
	return (
		<Layout isLeftSidebar={false}>
			<Wrap>
				<Slider {...settings}>
					<SlideWrap>
						<SlideInner>
							<TextWrap>
								<h2>
									ONECOIN <span>모의투자</span>
								</h2>
								<p>
									코인의 흐름에 대한 실전감각을 익힘으로써 실제 코인투자를
									준비합시다.
									<br />
									사이트에서 제공하는 모의주식투자를 통해 즐거운 투자의 실전을
									체험해 볼 수 있습니다.
								</p>
								<p>
									한 달간 모의 투자 결과를 통해 사용자 간 수익 랭킹을 나열해
									드립니다.
									<br />
									나의 투자 능력을 확인 해 볼 수 있습니다.
								</p>
								<Link className="button" to="/exchange">
									체험하러 가기
								</Link>
							</TextWrap>
							<Lottie
								loop
								animationData={exchangeJson}
								play
								style={{ width: 500 }}
							/>
						</SlideInner>
					</SlideWrap>
					<SlideWrap>
						<SlideInner>
							<TextWrap>
								<h2>
									ONECOIN <span>실시간 채팅</span>
								</h2>
								<p>
									실시간으로 사이트내의 사용자들과 가상자산의 정보와 의견을
									<br />
									자유롭게 공유 할 수 있습니다.
								</p>
								<p>
									사이트 우측 하단 실시간 채팅 버튼을 클릭하면
									<br />
									실시간 채팅에 참여하실 수 있습니다.
								</p>
							</TextWrap>
							<Lottie
								loop
								animationData={chatJson}
								play
								style={{ width: 450 }}
							/>
						</SlideInner>
					</SlideWrap>
				</Slider>
			</Wrap>
		</Layout>
	);
};
export default Main;
